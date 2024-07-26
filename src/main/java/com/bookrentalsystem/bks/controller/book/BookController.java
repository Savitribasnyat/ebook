package com.bookrentalsystem.bks.controller.book;

import com.bookrentalsystem.bks.dto.author.AuthorResponse;
import com.bookrentalsystem.bks.dto.book.BookRequest;
import com.bookrentalsystem.bks.dto.book.BookResponse;
import com.bookrentalsystem.bks.dto.book.FileDto;
import com.bookrentalsystem.bks.dto.category.CategoryResponse;
import com.bookrentalsystem.bks.enums.BookRentStatus;
import com.bookrentalsystem.bks.exception.globalexception.BookCanNotBeDeletedException;
import com.bookrentalsystem.bks.exception.globalexception.BookNotAvailableCurrently;
import com.bookrentalsystem.bks.model.*;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.repo.BookContentRepository;
import com.bookrentalsystem.bks.repo.BookRepo;
import com.bookrentalsystem.bks.repo.RatingRepo;
import com.bookrentalsystem.bks.repo.UserRepo;
import com.bookrentalsystem.bks.service.*;
import com.bookrentalsystem.bks.service.bookserviceimpl.BookExcelImportService;
import com.bookrentalsystem.bks.service.bookserviceimpl.ExcelHelper;
import com.bookrentalsystem.bks.utility.Fileutils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final TransactionService transactionService;
    private final Fileutils fileutils;
    private final BookExcelImportService bookExcel;
    private final BookContentService bookContentService;
    private final UserRepo userRepo;
    private final RatingRepo ratingRepo;
    private final BookContentRepository bookContentRepository;
    private final BookRepo bookRepo;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/table")
    public String bookTable(Model model) {
        List<BookResponse> books = bookService.allBooks();
        model.addAttribute("book", books);
        model.addAttribute("fileChoose",new FileDto());
        return "book/BookTable";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/form")
    public String bookForm(Model model) {
        List<AuthorResponse> authors = authorService.allAuthor();
        List<CategoryResponse> categories = categoryService.allCategory();
        model.addAttribute("author", authors);
        model.addAttribute("category", categories);
        if (model.getAttribute("book") == null) {
            model.addAttribute("book", new BookRequest());
        }
        return "book/BookForm";
    }

    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") BookRequest bookRequest,
                           BindingResult result,
                           Model model, RedirectAttributes redirectAttributes) throws IOException {


        //multipart file validation
        if (bookRequest.getImageFile().getSize() == 0 && bookRequest.getId() == null) {
            String message = "Please select photo of your book";

            //field error is used to create the error of respective field and store the error info in BindingResult
            FieldError fieldError =
                    new FieldError("bookRequest", "imageFile", message);
            result.addError(fieldError);
        }

        //THIS IS THE VALIDATION FOR MULTIPART FILE S
        if (bookRequest.getId() == null) {
            //extract the type of the multipart file
            String type = fileutils.photoValidation(bookRequest.getImageFile());

            boolean validType = type.equals("image/jpeg") || type.equals("image/png");

            //check if the multipart file is in Jpg, png format
            if (!validType) {
                String message = "Image type should be jpg or png";
                FieldError fieldError =
                        new FieldError("bookRequest", "imageFile", message);
                result.addError(fieldError);
            }
        }

        if (result.hasErrors()) {
            List<AuthorResponse> authors = authorService.allAuthor();
            List<CategoryResponse> categories = categoryService.allCategory();
            model.addAttribute("author", authors);
            model.addAttribute("category", categories);
            model.addAttribute("book", bookRequest);
            return "book/BookForm";
        }
       String message = bookService.addBook(bookRequest);
        if(message==null){
            redirectAttributes.addFlashAttribute("message", "Book table updated");
        }

        return "redirect:/book/table";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        List<Transaction> transactions = transactionService.allTransactionEntity();

        List<Transaction> notDeleteTransaction = transactions.stream().filter(t -> t.getBook().getId().equals(id))
                .filter(t -> t.getStatus().equals(BookRentStatus.RENT)).toList();

        if (notDeleteTransaction.isEmpty()) {
            bookService.deleteBook(id);
        } else {
            throw new BookCanNotBeDeletedException("Book cannot be deleted");
        }
        redirectAttributes.addFlashAttribute("message", "Book Deleted Successfully!!");
        return "redirect:/book/table";
    }

    //update book
    @GetMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Book book = bookService.findBookByid(id);
        BookResponse bookResponse = bookService.entityTBookResponse(book);
        redirectAttributes.addFlashAttribute("book", bookResponse);
        return "redirect:/book/form";
    }

    //view the book details in separate page
    @GetMapping("view/{id}")
    public String viewBook(@PathVariable Long id, Model model
            ,Principal principal) throws IOException {
        BookResponse singleBook = bookService.viewBookId(id);
        Optional<User> userOptional = userRepo.findUserByEmail(principal.getName());
        Optional<Rating> ratingOptional = ratingRepo.findRatingByUserAndBook(userOptional.get().getId(),id);
        if(ratingOptional.isPresent()){
            model.addAttribute("rating",ratingOptional.get().getRatingNumber());   //if the rating is given by user
        }
        Category category = categoryService.findCategoryById(singleBook.getCategoryId());
        model.addAttribute("book", singleBook);  //MODEL TO DISPLAY BOOK INFO IN PAGE
        model.addAttribute("category",category);
        return "book/BookDetail";
    }

    @GetMapping("/view/name/{name}")
    public String viewBookByName(@PathVariable String name, Model model
            ,Principal principal) throws IOException {
        Optional<Book> bookView = bookRepo.findByBookNameForView(name);
        if(bookView.isPresent()){
            BookResponse singleBook = bookService.viewBookId(bookView.get().getId());
            Optional<User> userOptional = userRepo.findUserByEmail(principal.getName());
            Optional<Rating> ratingOptional = ratingRepo.findRatingByUserAndBook(userOptional.get().getId(),bookView.get().getId());
            if(ratingOptional.isPresent()){
                model.addAttribute("rating",ratingOptional.get().getRatingNumber());   //if the rating is given by user
            }
            Category category = categoryService.findCategoryById(singleBook.getCategoryId());
            model.addAttribute("book", singleBook);  //MODEL TO DISPLAY BOOK INFO IN PAGE
            model.addAttribute("category",category);
            return "book/BookDetail";
        }
        throw new BookNotAvailableCurrently("Book not available currently");
    }

    @GetMapping("/download/content/{id}")
    public ResponseEntity<Resource> downloadBookContent(@PathVariable Long id) throws IOException {
        BookContent bookContent = bookContentService.findById(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename="+ bookContent.getFileName())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new ByteArrayResource(bookContent.getData()));
    }

    @GetMapping("/read/content/{id}/{name}")
    public ResponseEntity<Resource> readBookContent(@PathVariable Long id,@PathVariable String name) throws IOException {
        BookContent bookContent = bookContentService.findById(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename="+ bookContent.getFileName())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new ByteArrayResource(bookContent.getData()));
    }


    //upload book from Excel
    @PostMapping("/upload")
    public String upload(@ModelAttribute("fileChoose") FileDto file,
                         RedirectAttributes redirectAttributes) throws IOException {
        if(ExcelHelper.checkExcelFormat(file.getMultipartFile())){
          List<String> booksAdded =   bookExcel.save(file.getMultipartFile());
            redirectAttributes.addFlashAttribute("bookName",booksAdded);
        }else {
            redirectAttributes.addFlashAttribute("errorMessage","Please only select excel file");
        }
        return "redirect:/book/table";
    }

}
