package com.bookrentalsystem.bks.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    @NotBlank(message = "Please enter valid name")
//    @Pattern(regexp="^[A-Za-z]*$",message = "name is not valid")
    @Length(min = 3,max = 20,message = "name length should be above 3 words!!")
    private String name;

    @NotEmpty(message = "Please enter valid email")
    @Email(message = "Enter valid email!!")
    private String email;

    @NotEmpty(message = "Please enter valid password.")
    @Length(min = 3,max = 20,message = "Password should be between 3 to 20 digits!!")
    private String password;
}
