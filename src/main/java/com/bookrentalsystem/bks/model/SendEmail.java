package com.bookrentalsystem.bks.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendEmail {
    private String from;
    @Email(message = "Please enter valid email")
    private String to;
    private String subject;
    private String body;

}

