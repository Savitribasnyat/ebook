package com.bookrentalsystem.bks.exception.RestApiExceptionHandler;

import org.springframework.http.HttpStatus;

public class ApiException {
    private String message;
    private HttpStatus httpStatus;
    public ApiException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
