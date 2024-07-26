package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.dto.author.AuthorRequest;
import com.bookrentalsystem.bks.dto.author.AuthorResponse;
import com.bookrentalsystem.bks.model.Author;

import java.util.List;

public interface AuthorService {
     String addAuthorDb(AuthorRequest authorRequest);
     String updateAuthorAdd(AuthorRequest authorRequest);
     Author authorRequestToEntity(AuthorRequest authorRequest);
     AuthorResponse entityToAuthorResponse(Author author);
     List<Author> convertToAuthorList(List<Long> ids);
     List<AuthorResponse> convertToAuthorResponseList(List<Author> authors);
     List<AuthorResponse> allAuthor();
     List<Author> allAuthors();
     Author findAuthorById(Long id);
     AuthorResponse findAuthorResponseById(Long id);
     String deleteAuthor(Long id);
     AuthorResponse findAuthorNameResponseById(Long id);
}
