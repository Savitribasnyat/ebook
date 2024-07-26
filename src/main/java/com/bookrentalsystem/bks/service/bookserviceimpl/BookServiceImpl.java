package com.bookrentalsystem.bks.service.bookserviceimpl;

import com.bookrentalsystem.bks.dto.book.BookRequest;
import com.bookrentalsystem.bks.dto.book.BookResponse;
import com.bookrentalsystem.bks.exception.globalexception.BookCanNotBeDeletedException;
import com.bookrentalsystem.bks.model.Author;
import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.model.BookContent;
import com.bookrentalsystem.bks.repo.BookContentRepository;
import com.bookrentalsystem.bks.repo.BookRepo;
import com.bookrentalsystem.bks.service.AuthorService;
import com.bookrentalsystem.bks.service.BookService;
import com.bookrentalsystem.bks.service.CategoryService;
import com.bookrentalsystem.bks.utility.ConvertToLocalDateTime;
import com.bookrentalsystem.bks.utility.Fileutils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepo bookRepo;
    private final ConvertToLocalDateTime dateUtil;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final Fileutils fileutils;
    private final BookContentRepository bookContentRepository;

    //this method is used to add book
    public String addBook(BookRequest bookRequest) throws IOException {
        bookRequest.setName(bookRequest.getName().trim());
        bookRequest.setIsbn(bookRequest.getIsbn().trim());


        String imagePath = null;
        if (bookRequest.getId() != null && bookRequest.getImageFile().isEmpty()) {
            Book book = bookRepo.findById(bookRequest.getId()).orElseThrow();
            imagePath = book.getImage_path();
        } else {
            imagePath = fileutils.saveMultipartFile(bookRequest.getImageFile());

        }

        //save book content
        String fileName = StringUtils.cleanPath(bookRequest.getContentFile().getOriginalFilename());
        BookContent bookContent = new BookContent(
                fileName,bookRequest.getContentFile().getContentType(),bookRequest.getContentFile().getBytes()
        );

        Book book = Book.builder()
                .id(bookRequest.getId())
                .name(bookRequest.getName())
                .page(100)
                .isbn(bookRequest.getIsbn())
                .rating(5.0)
                .stock(1)
                .description(bookRequest.getDescription())
                .bookContent(bookContent)
                .published_date(dateUtil.convertToDate(bookRequest.getPublished_date()))
                .category(categoryService.findCategoryById(bookRequest.getCategoryId()))
                .authors(authorService.convertToAuthorList(bookRequest.getAuthorsId()))
                .image_path(imagePath)
                .deleted(false)
                .build();
        bookRepo.save(book);


        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();

        BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .page(book.getPage())
                .isbn(book.getIsbn())
                .rating(book.getRating())
                .stock(book.getStock())
                .published_date(book.getPublished_date().toString())
                .image_path(book.getImage_path())
                .categoryId(book.getCategory().getId())
                .authorsId(authorIds)
                .build();

        return null;
    }

    //this is used to find book by id
    @Override
    public Book findBookByid(Long id) {
        Optional<Book> singleBook = bookRepo.findById(id);
        if (singleBook.isPresent()) {
            return singleBook.get();
        }
        throw new BookCanNotBeDeletedException("Book does not exist!!!");
    }

    //method which return List of bookResponse dto
    @Override
    public List<BookResponse> allBooks() {
        List<Book> allBooks = bookRepo.findAll();
        return entityListToBookResponse(allBooks);
    }

    //method to delete book
    @Override
    public String deleteBook(Long id) {
        Book book = findBookByid(id);
        bookContentRepository.deleteById(book.getBookContent().getId());
        bookRepo.deleteById(id);
        return "deleted";
    }


    //method used to convert entity Book to BookResponse dto
    public BookResponse entityTBookResponse(Book book) {
        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .page(book.getPage())
                .isbn(book.getIsbn())
                .rating(book.getRating())
                .stock(book.getStock())
                .published_date(book.getPublished_date().toString())
                .image_path(book.getImage_path())
                .categoryId(book.getCategory().getId())
                .authorsId(authorIds)
                .description(book.getDescription())
                .build();
    }

    public List<BookResponse> entityListToBookResponse(List<Book> books) {

        return books
                .stream().map(this::entityTBookResponse).toList();
    }

    // this function is used to find book by its id & return BookReponse
    @Override
    public BookResponse viewBookId(Long id) throws IOException {
        Optional<Book> singleBook = bookRepo.findById(id);

        if (singleBook.isPresent()) {
            Book book = singleBook.get();
            List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();


            return BookResponse.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .page(book.getPage())
                    .isbn(book.getIsbn())
                    .rating(book.getRating())
                    .stock(book.getStock())
                    .description(book.getDescription())
                    .bookContentId(book.getBookContent().getId())
                    .published_date(book.getPublished_date().toString())
                    .image_path(fileutils.getBase64FormFilePath(book.getImage_path()))
                    .categoryId(book.getCategory().getId())
                    .authorsId(authorIds)
                    .build();
        }
        throw new BookCanNotBeDeletedException("Book does not exist!!!");
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepo.save(book);
    }

    //list of Book entity
    @Override
    public List<Book> allBookEntity() {
        return bookRepo.findAll();
    }

    //this method is used to convert list of book to list of book response
    @Override
    public List<BookResponse> allBookView() {
       List<Book> allBooks = bookRepo.findAll();
        return allBooks.stream().map(book -> {
            try {
                return forViewBook(book);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Override
    public List<BookResponse> featuredBooks() {
        List<Book> allBooks = bookRepo.featuredBooks();
        return allBooks.stream().map(book -> {
            try {
                return forViewBook(book);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    //THIS METHOD IS USED TO SHOW ALL THE BOOKs FOR LANDING PAGE
    @Override
    public BookResponse forViewBook(Book book) throws IOException {
        List<Long> authorIds = book.getAuthors()
                .stream().map(Author::getId).toList();
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .page(book.getPage())
                .isbn(book.getIsbn())
                .rating(book.getRating())
                .stock(book.getStock())
                .published_date(book.getPublished_date().toString())
                .image_path(fileutils.getBase64FormFilePath(book.getImage_path()))
                .categoryId(book.getCategory().getId())
                .authorsId(authorIds)
                .build();
    }
}
