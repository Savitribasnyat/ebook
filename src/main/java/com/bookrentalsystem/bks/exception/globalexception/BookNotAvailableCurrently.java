package com.bookrentalsystem.bks.exception.globalexception;

public class BookNotAvailableCurrently extends RuntimeException{
    public BookNotAvailableCurrently(String message) {
        super(message);
    }
}
