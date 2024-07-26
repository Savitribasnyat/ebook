package com.bookrentalsystem.bks.dto.author;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorRequest {
    private Long id;
    @NotBlank(message = "Name should not be blank")
    @Length(max = 100,min = 3,message = "Name should be between 3 to 100 word ")
    @Pattern(regexp="^[a-zA-Z].*[\\s\\.]*$",message = "name is not valid")
    private String name;

    @Email(message = "email should be in format")
    @NotEmpty(message = "Please enter author email address")
    private String email;

    @Length(max = 10,min = 10,message = "Phone number should be 10 digit")
    @Pattern(regexp="^[0-9]*$",message = "phone number should be numeric")
    private String number;
}
