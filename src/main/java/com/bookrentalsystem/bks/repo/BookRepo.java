package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepo extends JpaRepository<Book,Long> {
    Optional<Book> findByNameAndDeletedIsFalse(String name);
    @Query(nativeQuery = true,value = "select * from book where book.book_name=?1 and book.deleted=true")
    Optional<Book> findByNameAndDeletedIsTrue(String name);
    @Query(nativeQuery = true,value = "select * from book where book.book_name=?1")
    Optional<Book> findByBookNameForView(String name);
    @Query(nativeQuery = true,value = "select * from book where book_name ilike '%' || ?1 || '%'")
    List<Book> findByName(String name);
    @Query(nativeQuery = true,value = "select * from book where book.id=?1")
    Book findByBookId(Long id);

    @Query(nativeQuery = true,value = "SELECT * FROM book ORDER BY random() LIMIT 8;")
    List<Book> featuredBooks();

}
