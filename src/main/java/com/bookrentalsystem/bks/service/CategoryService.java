package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.dto.category.CategoryRequest;
import com.bookrentalsystem.bks.dto.category.CategoryResponse;
import com.bookrentalsystem.bks.model.Category;

import java.util.List;

public interface CategoryService {
     String addCategory(CategoryRequest categoryRequest);
     String saveUpdateCategory(CategoryRequest categoryRequest);
     Category findCategoryById(Long id);
     CategoryResponse entityToCategory(Category category);
     Category categoryRequestToEntity(CategoryRequest categoryRequest);
     List<CategoryResponse> allCategory();
     CategoryResponse findCategoryResponseById(Long id);
     CategoryResponse findCategoryResponseByIdForBook(Long id);
     String deleteCategory(Long id);
     List<CategoryResponse> categoryNotDeleted();

}
