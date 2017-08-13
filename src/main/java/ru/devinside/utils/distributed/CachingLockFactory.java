package ru.devinside.utils.distributed;

import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class CachingLockFactory<T> implements LockFactory<T> {
    private final Map<T, LockWrapper> locks = new ConcurrentHashMap<>();

    private final LockFactory<T> lockFactory;

    public CachingLockFactory(LockFactory<T> lockFactory) {
        this.lockFactory = lockFactory;
    }

    @Override
    public Lock createLock(T lockKey) {
        return locks.computeIfAbsent(lockKey, k -> new LockWrapper(k, lockFactory.createLock(k)));
    }

    private class LockWrapper implements Lock {
        private final T lockKey;
        private final Lock lock;

        public LockWrapper(T lockKey, Lock lock) {
            this.lockKey = lockKey;
            this.lock = lock;
        }

        @Override
        public void lock() {
            lock.lock();
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            lock.lockInterruptibly();
        }

        @Override
        public boolean tryLock() {
            return lock.tryLock();
        }

        @Override
        public boolean tryLock(long time, @Nonnull TimeUnit unit) throws InterruptedException {
            return lock.tryLock(time, unit);
        }

        @Override
        public void unlock() {
            LockWrapper w = locks.remove(lockKey);
            Assert.isTrue(w != null && w == lock, String.format("Locks cache integrity violation for key %s ", lockKey));
            lock.unlock();
        }

        @Nonnull
        @Override
        public Condition newCondition() {
            return lock.newCondition();
        }
    }
}
