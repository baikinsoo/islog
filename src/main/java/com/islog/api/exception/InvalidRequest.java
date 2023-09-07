package com.islog.api.exception;

/*
* status -> 400
* */

import lombok.Getter;

@Getter
public class InvalidRequest extends IslogException {

    private static final String MESSAGE = "잘못된 요청입니다.";

//    public String fieldName;
//    public String message;
    // 필드를 직접적으로 설정하는 것은 좋지 않다...

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
//        this.fieldName = fieldName;
//        this.message = message;
        addValidation(fieldName, message);
    }

    @Override
    public int getStatuscode() {
        return 400;
    }
}
