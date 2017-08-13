package ru.devinside.utils.distributed;

public class LockingException extends RuntimeException {
    public LockingException(Throwable cause) {
        super(cause);
    }
}
