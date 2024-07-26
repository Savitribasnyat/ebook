package com.bookrentalsystem.bks.exception.globalexception;

public class AuthorBookNotFound extends RuntimeException{
    public AuthorBookNotFound(String message) {
        super(message);
    }
}

