package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.RequestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestBookRepo extends JpaRepository<RequestBook,Long> {
    @Query(nativeQuery = true,value = "select *\n" +
            "from book_request where bookrequest_user = ?1")
    List<RequestBook> requestBookListByUser(Long userId);

    @Query(nativeQuery = true,
            value = "select case when count(*) > 0 then true " +
            "else false end as bookStatus from " +
            "book where book_name = ?1")
    Boolean bookStatus(String bookName);

}
