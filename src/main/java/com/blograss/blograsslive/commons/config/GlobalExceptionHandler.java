package com.blograss.blograsslive.commons.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class) // 모든 종류의 예외를 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 에러 반환
    public String handleAllExceptions(Exception ex) {
        logger.error("An unexpected error occurred", ex);
        return "An unexpected error occurred";
    }
}