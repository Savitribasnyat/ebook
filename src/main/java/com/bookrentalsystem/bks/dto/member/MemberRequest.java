package com.bookrentalsystem.bks.dto.member;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequest {
    private Long id;
    @NotBlank(message = "Please enter member name")
    @Pattern(regexp="^[a-zA-Z].*[\\s\\.]*$",message = "name is not valid")
    @Length(min = 3,max = 60,message = "name length should be above 3 words!!")
    private String name;

    @Email(message = "please enter email in proper format")
    @NotEmpty(message = "Please enter your email address")
    private String email;

    @NotBlank(message = "Please enter your address!!!")
    private String address;

    @Pattern(regexp="^[0-9]*$",message = "phone number should be numeric")
    @Length(min = 10,max = 10,message = "Phone number should be of 10 digit!!!")
    private String phone;
}
