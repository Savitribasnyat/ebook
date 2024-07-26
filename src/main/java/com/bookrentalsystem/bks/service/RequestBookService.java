package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.model.RequestBook;
import org.apache.catalina.LifecycleState;

import java.security.Principal;
import java.util.List;

public interface RequestBookService {
    String saveRequestBook(RequestBook requestBook, Principal principal);
    List<RequestBook> requestedBooks();
}
