package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.dto.forgotpassword.ForgotPasswordDto;
import com.bookrentalsystem.bks.model.ForgotPassword;
import com.bookrentalsystem.bks.model.SendEmail;
import jakarta.mail.MessagingException;

public interface ForgotPasswordService {
    void sendEmail(SendEmail email) throws MessagingException;
    ForgotPassword changeDtoToEntity(ForgotPasswordDto forgotPasswordDto);
    String checkCodeOtp(Integer code);
}
