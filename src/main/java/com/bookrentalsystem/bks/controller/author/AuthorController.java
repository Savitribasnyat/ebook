package com.bookrentalsystem.bks.controller.author;

import com.bookrentalsystem.bks.dto.author.AuthorRequest;
import com.bookrentalsystem.bks.dto.author.AuthorResponse;
import com.bookrentalsystem.bks.dto.book.BookResponse;
import com.bookrentalsystem.bks.dto.category.CategoryResponse;
import com.bookrentalsystem.bks.exception.globalexception.AuthorBookNotFound;
import com.bookrentalsystem.bks.exception.globalexception.AuthorCanNotBeDeletedException;
import com.bookrentalsystem.bks.exception.globalexception.CategoryBookNotFound;
import com.bookrentalsystem.bks.model.Author;
import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.repo.AuthorRepo;
import com.bookrentalsystem.bks.service.AuthorService;
import com.bookrentalsystem.bks.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;
    private final AuthorRepo authorRepo;

    //author table
    @GetMapping("/table")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String authorTable(Model model){
      List<AuthorResponse> authors = authorService.allAuthor();
      model.addAttribute("author",authors);
        return "author/AuthorTable";
    }

    //author form page
    @GetMapping("/form")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String authorForm(Model model){
        if(model.getAttribute("author") == null) {
            model.addAttribute("author", new AuthorRequest());
        }
        return "author/AuthorForm";
    }
//save author in the db
    @PostMapping("/save")
    public String saveAuthor(@Valid @ModelAttribute("author") AuthorRequest authorRequest,
                             BindingResult result,
                             Model model,RedirectAttributes redirectAttributes){

        if(result.hasErrors()){
            model.addAttribute("author",authorRequest);
            return "/author/AuthorForm";
        }

          String  message = authorService.addAuthorDb(authorRequest);

        if(message != null){
            redirectAttributes.addFlashAttribute("message","Author table updated!!");
            return "redirect:/author/table";
        }
        return "redirect:/author/table";
    }

    @GetMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id, Model model){
      AuthorResponse authorResponse = authorService.findAuthorResponseById(id);
      model.addAttribute("author",authorResponse);
      return "/author/UpdateForm";
    }

    //delete author controller
    @RequestMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id,RedirectAttributes redirectAttributes){
        List<Book> allBooks = bookService.allBookEntity();

       List<Long> allIds = allBooks.stream()
               .flatMap(b -> b.getAuthors().stream())
               .map(Author::getId).toList();

       Optional<Long> singleId = allIds.stream().filter(i -> i.equals(id)).findFirst();

       //if the id is present in the book then it will throw exception
       if(singleId.isPresent()){
           throw new AuthorCanNotBeDeletedException("Author cannot be deleted!!");
       }
       authorService.deleteAuthor(id);
        redirectAttributes.addFlashAttribute("message","Author Deleted Successfully!!");
        return "redirect:/author/table?success";
    }

    //list author
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER')")
    public String authorList(Model model){
        List<Author> authors = authorRepo.authorsNotDeleted();
        model.addAttribute("authors",authors);
        return "author/AuthorNameList";
    }

    @GetMapping("/book/list/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String authorBookList(@PathVariable Long id, Model model){
        AuthorResponse author = authorService.findAuthorNameResponseById(id);
        List<Long> bookIds =  authorRepo.getBooksIdFromAuthorId(id);
        if(bookIds.isEmpty()){
            throw new AuthorBookNotFound("Book of this category not available");
        }
        List<BookResponse> books =  bookIds.stream().map(bookId -> {
            try {
                return bookService.viewBookId(bookId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        model.addAttribute("books",books);
        model.addAttribute("author",author);
        return "author/AuthorBooks";
    }


}
