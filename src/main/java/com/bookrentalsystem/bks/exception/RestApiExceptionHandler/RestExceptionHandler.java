package com.bookrentalsystem.bks.exception.RestApiExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> userNotFound(UserNotFoundException userNotFoundException){
        ApiException apiException = new ApiException(
                userNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(apiException,HttpStatus.NOT_FOUND);
    }
}
