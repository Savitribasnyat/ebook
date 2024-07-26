package com.bookrentalsystem.bks.repo;

import com.bookrentalsystem.bks.model.Author;
import com.bookrentalsystem.bks.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Optional<Category> findCategoryByNameAndDeletedIsFalse(String name);
    @Query(nativeQuery = true,value = "select * from category where category.category_name=?1 and category.deleted=true")
    Optional<Category> findCategoryByNameAndDeletedIsTrue(String name);

    @Query(nativeQuery = true,value = "select category_name from category")
    List<String> getAllCategoryName();

    @Query(value = "select * from category where deleted = false",nativeQuery = true)
    List<Category> categoryNotDeleted();
    @Query(nativeQuery = true,value = "select id from book where category_id = ?1")
    List<Long> booksIdFromCategory(Long categoryId);
 }
