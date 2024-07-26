package com.bookrentalsystem.bks.service.requestbookserviceimpl;

import com.bookrentalsystem.bks.enums.RequestedBook;
import com.bookrentalsystem.bks.model.RequestBook;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.repo.RequestBookRepo;
import com.bookrentalsystem.bks.repo.UserRepo;
import com.bookrentalsystem.bks.service.RequestBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestBookServiceImpl implements RequestBookService {
    private final RequestBookRepo requestBookRepo;
    private final UserRepo userRepo;
    @Override
    public String saveRequestBook(RequestBook requestBook, Principal principal) {
        Optional<User> user = userRepo.findUserByEmail(principal.getName());
        requestBook.setUser(user.get());
        requestBook.setStatus(RequestedBook.PENDING);
        requestBookRepo.save(requestBook);
        return "Request is sent successfully ";
    }

    @Override
    public List<RequestBook> requestedBooks() {
        List<RequestBook> books = requestBookRepo.findAll();
        List<RequestBook> requestedBooks = new ArrayList<>();
        for(RequestBook book: books){
            if(requestBookRepo.bookStatus(book.getName())){
                book.setStatus(RequestedBook.AVAILABLE);
            }else{
                book.setStatus(RequestedBook.PENDING);
                requestedBooks.add(book);
            }
        }
        return requestedBooks;
    }
}
