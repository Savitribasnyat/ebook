package com.bookrentalsystem.bks.service.bookcontentimpl;

import com.bookrentalsystem.bks.exception.globalexception.CodeNotFoundException;
import com.bookrentalsystem.bks.model.BookContent;
import com.bookrentalsystem.bks.repo.BookContentRepository;
import com.bookrentalsystem.bks.service.BookContentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookContentServiceImpl implements BookContentService {
    private final BookContentRepository bookContentRepository;
    @Override
    public BookContent findById(Long id) {
        return bookContentRepository
                .findById(id)
                .orElseThrow(() -> new CodeNotFoundException("File not found with Id: " + id));
    }
}
