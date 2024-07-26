package com.bookrentalsystem.bks.utility;

import org.springframework.stereotype.Component;

@Component
public class GenerateRandomNumber {
    private GenerateRandomNumber(){}
    public static Integer generateRandomNumber(){
        int min=10000;
        int max = 99999;
        return (int)Math.floor(Math.random() * (max - min + 1) + min);
    }

}
