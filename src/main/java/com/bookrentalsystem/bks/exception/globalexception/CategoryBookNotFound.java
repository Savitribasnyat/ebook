package com.bookrentalsystem.bks.exception.globalexception;

public class CategoryBookNotFound extends RuntimeException{
    public CategoryBookNotFound(String message){
        super(message);
    }
}
