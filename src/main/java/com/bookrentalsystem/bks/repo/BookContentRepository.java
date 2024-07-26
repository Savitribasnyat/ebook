package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookContentRepository extends JpaRepository<BookContent,Long> {
}
