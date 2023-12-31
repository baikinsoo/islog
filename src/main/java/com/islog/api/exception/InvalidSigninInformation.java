package com.islog.api.exception;

public class InvalidSigninInformation extends IslogException{

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatuscode() {
        return 400;
    }
}
