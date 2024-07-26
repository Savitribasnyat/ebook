package com.bookrentalsystem.bks.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
//@Entity
public class ReturnBookTable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer code;
    private LocalDateTime from_date;
    private LocalDate returnDate;
    private String member_name;
    private String book_name;
}
