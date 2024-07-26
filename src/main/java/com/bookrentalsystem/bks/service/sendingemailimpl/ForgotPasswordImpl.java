package com.bookrentalsystem.bks.service.sendingemailimpl;

import com.bookrentalsystem.bks.dto.forgotpassword.ForgotPasswordDto;
import com.bookrentalsystem.bks.model.ForgotPassword;
import com.bookrentalsystem.bks.model.SendEmail;
import com.bookrentalsystem.bks.repo.ForgotPasswordRepo;
import com.bookrentalsystem.bks.service.ForgotPasswordService;
import com.bookrentalsystem.bks.utility.GenerateRandomNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ForgotPasswordImpl implements ForgotPasswordService {
    private final JavaMailSender javaMailSender;
    private final GenerateRandomNumber generateRandomNumber;
    private final ForgotPasswordRepo forgotPasswordRepo;


    //this method is used to send email
    @Async
    @Override
    public void sendEmail(SendEmail email) {
        Integer otpCode = GenerateRandomNumber.generateRandomNumber();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("manish.pudasaini177@yopmail.com");
        simpleMailMessage.setTo(email.getTo());
        simpleMailMessage.setText(" Please use this OTP to change your password " + "   "+otpCode );
        simpleMailMessage.setSubject("Change Password");

//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        Properties properties = mailSender.getJavaMailProperties();
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.transport.protocol", "smtp");

//        mailSender.setJavaMailProperties(properties);

        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto(email.getTo(), otpCode);
        forgotPasswordRepo.save(changeDtoToEntity(forgotPasswordDto));

        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public ForgotPassword changeDtoToEntity(ForgotPasswordDto forgotPasswordDto) {
        return ForgotPassword.builder()
                .email(forgotPasswordDto.getEmail())
                .code(forgotPasswordDto.getCode())
                .build();
    }

    //this is used to check otp code which is present in the database or not
    @Override
    public String checkCodeOtp(Integer code) {
       Optional<ForgotPassword> forgotPasswordCode =  forgotPasswordRepo.findByCode(code);
       if(forgotPasswordCode.isPresent()){
           forgotPasswordRepo.delete(forgotPasswordCode.get());
           return "Please change your password";
       }
        return null;
    }


}
