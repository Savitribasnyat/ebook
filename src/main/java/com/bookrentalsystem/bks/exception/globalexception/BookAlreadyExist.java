package com.bookrentalsystem.bks.exception.globalexception;

public class BookAlreadyExist extends RuntimeException{
    public BookAlreadyExist(String message) {
        super(message);
    }
}
