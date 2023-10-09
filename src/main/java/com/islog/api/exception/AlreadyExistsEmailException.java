package com.islog.api.exception;

public class AlreadyExistsEmailException extends IslogException{

    private static String MESSAGE = "이미 가입된 이메일입나다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatuscode(){
        return 400;
    }
}
