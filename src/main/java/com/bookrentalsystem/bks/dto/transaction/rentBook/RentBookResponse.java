package com.bookrentalsystem.bks.dto.transaction.rentBook;

import com.bookrentalsystem.bks.enums.BookRentStatus;
import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentBookResponse {
    private Long id;
    private String code;
    private LocalDate from;
    private LocalDate to;
    private BookRentStatus status;
    private Book book;
    private Member member;
}
