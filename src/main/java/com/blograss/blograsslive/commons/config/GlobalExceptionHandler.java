package com.blograss.blograsslive.commons.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // 모든 종류의 예외를 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 에러 반환
    public String handleAllExceptions(Exception ex) {
        return "An unexpected error occurred"; // 혹은 다른 사용자 정의 메시지를 반환할 수 있습니다.
    }
}