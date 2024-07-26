package com.bookrentalsystem.bks.exception.globalexception;

public class CodeNotFoundException extends RuntimeException{
    public CodeNotFoundException(String message) {
        super(message);
    }
}
