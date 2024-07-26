package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepo extends JpaRepository<Rating,Long> {
    @Query(nativeQuery = true,value = "select * from rating where rating_user=?1")
    List<Rating> findRatingByUser(Long userId);

    @Query(nativeQuery = true,value = "select *\n" +
            "from rating where (rating_user = ?1 and rating_book = ?2)")
    Optional<Rating> findRatingByUserAndBook(Long userId, Long bookId);
}
