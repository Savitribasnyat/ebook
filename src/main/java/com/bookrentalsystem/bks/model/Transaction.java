package com.bookrentalsystem.bks.model;

import com.bookrentalsystem.bks.enums.BookRentStatus;
import com.bookrentalsystem.bks.model.auditing.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "transaction",uniqueConstraints = {
        @UniqueConstraint(name = "uk_transaction_code",columnNames = "transaction_code")
})
public class Transaction extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_code",length = 5,nullable = false)
    private String code;

    @Column(name = "from_date",nullable = false)
    private LocalDate from;

    @Column(name = "to_date",nullable = false)
    private LocalDate to;

    @Column(name = "book_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private BookRentStatus status;

    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id",foreignKey = @ForeignKey(name = "fk_transaction_bookId"))
    @JsonIgnore
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",foreignKey = @ForeignKey(name = "fk_transaction_memberId"))
    @JsonIgnore
    private Member member;
}
