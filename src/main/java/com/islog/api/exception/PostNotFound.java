package com.islog.api.exception;

/*
*   status -> 404
* */
public class PostNotFound extends IslogException{

    private static final String MESSAGE = "존재하지 않는 글 입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatuscode() {
        return 404;
    }
}
