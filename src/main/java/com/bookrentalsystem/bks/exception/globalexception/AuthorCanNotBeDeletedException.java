package com.bookrentalsystem.bks.exception.globalexception;

public class AuthorCanNotBeDeletedException extends RuntimeException{
    public AuthorCanNotBeDeletedException(String message) {
        super(message);
    }
}
