package com.bookrentalsystem.bks.exception.globalexception;

public class CategoryCanNotBeDeletedException extends RuntimeException{
    public CategoryCanNotBeDeletedException(String message) {
        super(message);
    }
}
