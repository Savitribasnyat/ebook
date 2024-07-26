package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author,Long> {
    @Query(nativeQuery = true,value = "select * from author where author.email=?1 and author.deleted=true")
    Optional<Author> findByEmailAndDeletedIsTrue(String email);

//    Optional<Author> findByNumberAndDeletedIsFalse(String number);
    @Query(nativeQuery = true,value = "select * from author where author.phone_number=?1 and author.deleted=true")
    Optional<Author> findByNumberAndDeletedIsTrue(String number);

    @Query(value = "select * from author",nativeQuery = true)
    List<Author> authorNameList();
    @Query(value = "select * from author where deleted = false",nativeQuery = true)
    List<Author> authorsNotDeleted();

    @Query(nativeQuery = true,value = "select book_id from book_author_table where authors_id = ?1")
    List<Long> getBooksIdFromAuthorId(Long authorId);
}
