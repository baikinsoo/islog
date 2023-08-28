package com.islog.api.response;

/*
* code : 400
* message : "잘못된 요청입니다.
* --- 여기까지만 보내면 어디서 잘못되었는지 알 수 없다.
*
* validation을 통해 어느 필드에서 잘못되었는지 알려준다.
* validation : {
*   title : 값을 입력해주세요
* }
*
*
* validation : {
*   title : 값을 입력해주세요
* }
* */

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
