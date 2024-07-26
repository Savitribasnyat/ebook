package com.bookrentalsystem.bks.controller.requestbook;

import com.bookrentalsystem.bks.enums.RequestedBook;
import com.bookrentalsystem.bks.model.RequestBook;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.repo.RequestBookRepo;
import com.bookrentalsystem.bks.repo.UserRepo;
import com.bookrentalsystem.bks.service.RequestBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/request/book")
public class RequestBookController {
    private final RequestBookRepo requestBookRepo;
    private final RequestBookService requestBookService;
    private final UserRepo userRepo;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/main/table")
    public String requestBookMainTable(Model model){
        List<RequestBook> requestBookList =  requestBookService.requestedBooks();
        model.addAttribute("books",requestBookList);
        return "requestbook/RequestedBookMainTable";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/table")
    public String requestBookUserTable(Model model,Principal principal){
        Optional<User> user = userRepo.findUserByEmail(principal.getName());
       List<RequestBook> requestBookList =  requestBookRepo.requestBookListByUser(user.get().getId());
       for(RequestBook book : requestBookList){
           if(requestBookRepo.bookStatus(book.getName())){
               book.setStatus(RequestedBook.AVAILABLE);
           }else {
               book.setStatus(RequestedBook.PENDING);
           }
       }
       model.addAttribute("books",requestBookList);
        return "requestbook/RequestBookTable";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/form")
    public String requestBook(Model model){
        model.addAttribute("requestBook",new RequestBook());
        return "requestbook/RequestBook";
    }

    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute RequestBook requestBook,
                           BindingResult result, Model model,
                           RedirectAttributes redirectAttributes, Principal principal){
        if(result.hasErrors()){
           model.addAttribute("requestBook",requestBook);
           return "requestbook/RequestBook";
        }

        String message = requestBookService.saveRequestBook(requestBook,principal);
        if(message == null){
            redirectAttributes.addFlashAttribute("message", "Book table updated");
            return "redirect:/request/book/table";
        }
        redirectAttributes.addFlashAttribute("message", "Book table updated");
        return "redirect:/request/book/table";
    }

}
