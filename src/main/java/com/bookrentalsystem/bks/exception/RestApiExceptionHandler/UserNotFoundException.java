package com.bookrentalsystem.bks.exception.RestApiExceptionHandler;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
