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

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
//비어있지 않은 데이터만 Json 데이터를 보여주겠다는 의미이다.
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
