package com.bookrentalsystem.bks.exception.globalexception;

public class UserHavingThisEmailNotExist extends RuntimeException{
    public UserHavingThisEmailNotExist(String message) {
        super(message);
    }
}
