package com.bookrentalsystem.bks.dto.transaction.returnBook;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnBookRequest {

    private String code;
    private String from;
    @NotEmpty(message = "Please enter the return date of Book")
    private String returnDate;
    private String member_name;
}
