package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.model.ReturnBookTable;
import com.bookrentalsystem.bks.model.Transaction;

import java.util.List;

public interface ReturnBookTbl {
    ReturnBookTable addReturnBook(Transaction transaction);
    List<ReturnBookTable> allReturnBooks();
    void deleteReturnBook(Long id);
}
