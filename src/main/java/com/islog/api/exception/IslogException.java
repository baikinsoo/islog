package com.islog.api.exception;

public abstract class IslogException extends RuntimeException {

    public IslogException(String message) {
        super(message);
    }

    public IslogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatuscode();
}
