package com.bookrentalsystem.bks.controller;

import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.repo.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final BookRepo bookRepo;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchCode(@PathVariable("query") String query){
        System.out.println(query);
        List<Book> allBook = bookRepo.findByName(query);
        return new ResponseEntity<>(allBook, HttpStatus.OK);
    }
}
