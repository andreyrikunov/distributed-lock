package ru.devinside.utils.distributed;

import java.util.concurrent.locks.Lock;

public interface LockFactory<T> {
    Lock createLock(T lockKey);

    default LockFactory<T> cachingLockFactory(LockFactory<T> f) {
        return new CachingLockFactory<>(f);
    }
}
