package com.bookrentalsystem.bks.exception.globalexception;

public class MemberCanNotBeDeletedException extends RuntimeException{
    public MemberCanNotBeDeletedException(String message) {
        super(message);
    }
}
