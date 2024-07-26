package com.bookrentalsystem.bks.controller.landingpage;

import com.bookrentalsystem.bks.dto.book.BookResponse;
import com.bookrentalsystem.bks.exception.globalexception.UserHavingThisEmailNotExist;
import com.bookrentalsystem.bks.filteringalgorithm.CollaborativeFiltering;
import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.repo.BookRepo;
import com.bookrentalsystem.bks.service.BookService;
import com.bookrentalsystem.bks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LandingPageController {
    private final BookService bookService;
    private final CollaborativeFiltering collaborativeFiltering;
    private final BookRepo bookRepo;
    private final UserService userService;
    //landing page view & list of all books
    @GetMapping("/home")
    public String homePage(Model model,Principal principal){
        int bookNumber = 10; //10 books will be recommended to login user
        User user = userService.findUsingEmail(principal.getName())
                .orElseThrow(()-> new UserHavingThisEmailNotExist("User does not exist"));
        List<Long> bookIds =  collaborativeFiltering.recommendBooks(user.getId(),bookNumber);
        List<Book> books = new ArrayList<>();
        for(Long id : bookIds){
            books.add(bookRepo.findByBookId(id));
        }
        List<BookResponse> bookRsponse = books.stream().map(book -> {
            try {
                return bookService.forViewBook(book);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        model.addAttribute("recommendateBook",bookRsponse);

        //Features books list
        List<BookResponse> featuresBooks = bookService.featuredBooks();
        model.addAttribute("books",featuresBooks);
        return "landingPage/LandingPage";
    }

//    @GetMapping("/recommendation/{bookNumber}")
//    public String rexommendationOfBook(@PathVariable int bookNumber, Principal principal,
//                                       RedirectAttributes redirectAttributes){
//        User user = userService.findUsingEmail(principal.getName())
//                .orElseThrow(()-> new UserHavingThisEmailNotExist("User does not exist"));
//       List<Long> bookIds =  collaborativeFiltering.recommendBooks(user.getId(),bookNumber);
//       List<Book> books = new ArrayList<>();
//       for(Long id : bookIds){
//           books.add(bookRepo.findByBookId(id));
//       }
//        List<BookResponse> bookRsponse = books.stream().map(book -> {
//            try {
//                return bookService.forViewBook(book);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).toList();
//
//        redirectAttributes.addFlashAttribute("recommendateBook",bookRsponse);
//        return "redirect:/home";
//
//    }

}
