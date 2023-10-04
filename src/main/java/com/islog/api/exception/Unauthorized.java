package com.islog.api.exception;

/*
 *   status -> 404
 * */
public class Unauthorized extends IslogException{

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatuscode() {
        return 401;
    }
}
