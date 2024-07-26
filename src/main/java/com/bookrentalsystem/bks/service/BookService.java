package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.dto.book.BookRequest;
import com.bookrentalsystem.bks.dto.book.BookResponse;
import com.bookrentalsystem.bks.model.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    String addBook(BookRequest bookRequest) throws IOException;
    Book findBookByid(Long id);
    List<BookResponse> allBooks();
    String deleteBook(Long id);
    BookResponse entityTBookResponse(Book book);
    BookResponse viewBookId(Long id) throws IOException;
    Book saveBook(Book book);
    List<Book> allBookEntity();

    List<BookResponse> allBookView();
    List<BookResponse> featuredBooks();
    BookResponse forViewBook(Book book) throws IOException;


}
