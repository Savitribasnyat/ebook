package com.bookrentalsystem.bks.utility;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ConvertToLocalDateTime {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public LocalDate convertToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(date, formatter);
    }
    public LocalDate convertToDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate currentTime = LocalDate.now();
        String date = currentTime.format(formatter);
        return LocalDate.parse(date,formatter);
    }

    public String convertDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }

}
