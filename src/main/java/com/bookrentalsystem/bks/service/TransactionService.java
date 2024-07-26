package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.dto.transaction.TransactionDto;
import com.bookrentalsystem.bks.dto.transaction.rentBook.RentBookRequest;
import com.bookrentalsystem.bks.dto.transaction.rentBook.RentBookResponse;
import com.bookrentalsystem.bks.dto.transaction.returnBook.ReturnBookRequest;
import com.bookrentalsystem.bks.model.Transaction;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    RentBookResponse rentABook(RentBookRequest rentBookRequest);

    Transaction rentBookRequestToTransacrion(RentBookRequest rentBookRequest);

    RentBookResponse transactionToRentBookResponse(Transaction transaction);

    List<RentBookResponse> allRentBooks();

    Transaction findTransactionByCode(String code);

    ReturnBookRequest transactionToReturnBook(Transaction transaction);

    Transaction saveTransaction(Transaction transaction);
    TransactionDto transactionToTransactionDto(Transaction transaction);
    List<TransactionDto> allTransaction();
    List<Transaction> allTransactionEntity();

    ByteArrayInputStream downloadHistoryInExcel() throws IOException;

    Page<TransactionDto> getPaginatedTransaction(Integer pageNo, Integer pageSize);

    Page<TransactionDto> findTransactionFromDate(Integer pageNo, Integer pageSize,LocalDate from, LocalDate to);
}
