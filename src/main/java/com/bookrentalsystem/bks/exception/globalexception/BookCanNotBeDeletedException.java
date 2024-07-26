package com.bookrentalsystem.bks.exception.globalexception;

public class BookCanNotBeDeletedException extends RuntimeException{
    public BookCanNotBeDeletedException(String message) {
        super(message);
    }
}
