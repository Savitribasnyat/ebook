package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.enums.BookRentStatus;
import com.bookrentalsystem.bks.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByCode(String code);

    //searching code according to the code name & status
    List<Transaction> findByCodeContainingAndStatus(String code, BookRentStatus status);

    @Query(nativeQuery = true, value = "select * from transaction where transaction.from_date BETWEEN ?1 And ?2 OR transaction.return_date BETWEEN ?1 And ?2")
    Page<Transaction> findByDate(LocalDate from, LocalDate returnDate,Pageable pageable);

}
