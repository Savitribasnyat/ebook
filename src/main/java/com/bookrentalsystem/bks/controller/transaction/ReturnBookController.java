package com.bookrentalsystem.bks.controller.transaction;

import com.bookrentalsystem.bks.dto.book.BookResponse;
import com.bookrentalsystem.bks.dto.transaction.returnBook.ReturnBookRequest;
import com.bookrentalsystem.bks.enums.BookRentStatus;
import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.model.Transaction;
import com.bookrentalsystem.bks.service.BookService;
import com.bookrentalsystem.bks.service.TransactionService;
import com.bookrentalsystem.bks.utility.ConvertToLocalDateTime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/return/book")
@RequiredArgsConstructor
public class ReturnBookController {
    private final TransactionService transactionService;
    private final ConvertToLocalDateTime convertToLocalDateTime;
    private final BookService bookService;

    //display return book form
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @GetMapping("/form")
    public String returnBookForm(Model model){
        if(model.getAttribute("transaction") == null){
           List<Transaction> allTransaction = transactionService.allTransactionEntity();
           List<String> allCodes = allTransaction.stream().map(Transaction::getCode).toList();

           model.addAttribute("transaction",new ReturnBookRequest());
           model.addAttribute("codes",allCodes);
        }
        return "transaction/returnBook/ReturnBookForm";
    }

    //check if the transaction code is valid or not
    @GetMapping("/code")
    public String getCodeTransaction(@ModelAttribute ReturnBookRequest returnBookRequest,
                                     RedirectAttributes redirectAttributes) throws IOException {
       Transaction transaction = transactionService.findTransactionByCode(returnBookRequest.getCode());
        ReturnBookRequest returnBook  = transactionService.transactionToReturnBook(transaction);

        //extract book from its id to show its image
        BookResponse singleBook =bookService.viewBookId(transaction.getBook().getId());
        redirectAttributes.addFlashAttribute("book",singleBook);

        redirectAttributes.addFlashAttribute("transaction",returnBook);
        return "redirect:/return/book/form";
    }

    @GetMapping("/{code}/api")
    public String getCodeTransactionCheck(@PathVariable("code") String code,
                                     RedirectAttributes redirectAttributes) throws IOException {
        Transaction transaction = transactionService.findTransactionByCode(code);
        ReturnBookRequest returnBook  = transactionService.transactionToReturnBook(transaction);

        //extract book from its id to show its image
        BookResponse singleBook =bookService.viewBookId(transaction.getBook().getId());
        redirectAttributes.addFlashAttribute("book",singleBook);

        redirectAttributes.addFlashAttribute("transaction",returnBook);
        return "redirect:/return/book/form";
    }

    //save the record of return book
    @PostMapping("/save")
    public String saveReturnTransaction(@Valid @ModelAttribute("transaction") ReturnBookRequest returnBookRequest,
                                        BindingResult bindingResult,Model model) throws IOException {
        if(bindingResult.hasErrors()){
            Transaction transaction = transactionService.findTransactionByCode(returnBookRequest.getCode());
            BookResponse singleBook =bookService.viewBookId(transaction.getBook().getId());
            model.addAttribute("book",singleBook);
            model.addAttribute("transaction",returnBookRequest);
           return "transaction/returnBook/ReturnBookForm";
        }

        String code = returnBookRequest.getCode();
        Transaction transaction = transactionService.findTransactionByCode(code);
        transaction.setStatus(BookRentStatus.RETURN);
        transaction.setReturnDate(convertToLocalDateTime.convertToDate(returnBookRequest.getReturnDate()));

        Long bookId = transaction.getBook().getId();
        Book singleBook = bookService.findBookByid(bookId);
//        singleBook.setStock(singleBook.getStock() +1);
        bookService.saveBook(singleBook);

        transactionService.saveTransaction(transaction);


        return "redirect:/transaction/table";
    }


}
