package com.bookrentalsystem.bks.dto.forgotpassword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordDto {
    private Long id;
    private String email;
    private Integer code;
    private Long userId;
    public ForgotPasswordDto(String email, Integer code) {
        this.email = email;
        this.code = code;
    }
}

