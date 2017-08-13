package ru.devinside.utils.distributed.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.devinside.utils.distributed.LockFactory;
import ru.devinside.utils.distributed.LockingException;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Service
public class CuratorLockFactory implements LockFactory<String> {
    private CuratorFramework curator;

    @Autowired
    public CuratorLockFactory(CuratorFramework curator) {
        this.curator = curator;
    }

    @Override
    public Lock createLock(String lockKey) {
        return new CuratorLock(new InterProcessMutex(curator, lockKey));
    }

    private class CuratorLock implements Lock {
        private final InterProcessLock lock;

        public CuratorLock(InterProcessLock lock) {
            this.lock = lock;
        }

        @Override
        public void lock() {
            try {
                lock.acquire();
            } catch (Exception e) {
                throw new LockingException(e);
            }
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean tryLock() {
            return tryLock(1, TimeUnit.MILLISECONDS);
        }

        @Override
        public boolean tryLock(long time, @Nonnull TimeUnit unit) {
            try {
                return lock.acquire(time, unit);
            } catch (Exception e) {
                throw new LockingException(e);
            }
        }

        @Override
        public void unlock() {
            try {
                lock.release();
            } catch (Exception e) {
                throw new LockingException(e);
            }
        }

        @Nonnull
        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }
}
