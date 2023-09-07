package com.islog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class IslogException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public IslogException(String message) {
        super(message);
    }

    public IslogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatuscode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
