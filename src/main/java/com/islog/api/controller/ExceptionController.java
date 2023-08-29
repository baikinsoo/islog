package com.islog.api.controller;

import com.islog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
//    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
//        System.out.println("하하하");
//        log.error("exceptionHandler error", e);
//        MethodArgumentNotValidException
//        -> 즉, Exception 클래스는 MethodArgumentNotValidException이 아니기 때문에 field 에러에 관한 내용이 담겨져있지 않다.
//        파라미터로 받는 클래스를 해당 MethodArgumentNotValidException으로 변경해준다.


//        if (e.hasErrors()) {
//            FieldError fieldError = e.getFieldError();
//            String field = fieldError.getField();
//            String message = fieldError.getDefaultMessage();

//        --------------------
//        ErrorResponse response = new ErrorResponse("400", "잘못된 요청입니다.");
//
//        for (FieldError fieldError : e.getFieldErrors()) {
//            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        return response;
//    }

//    --------------
//        Map<String, String> response = new HashMap<>();
//        response.put(field, message);
//        -> ----------------- error 클래스를 별도로 생성해서 사용할 것이다.
//        return response;

    //---------------builder를 사용-----------------------
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }
}