package com.bookrentalsystem.bks.service.categoryserviceimpl;

import com.bookrentalsystem.bks.dto.category.CategoryRequest;
import com.bookrentalsystem.bks.dto.category.CategoryResponse;
import com.bookrentalsystem.bks.exception.globalexception.CategoryBookNotFound;
import com.bookrentalsystem.bks.exception.globalexception.CategoryCanNotBeDeletedException;
import com.bookrentalsystem.bks.model.Category;
import com.bookrentalsystem.bks.repo.CategoryRepo;
import com.bookrentalsystem.bks.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    //this is the business logic to create category
    public String addCategory(CategoryRequest categoryRequest) {
        categoryRequest.setName(categoryRequest.getName().trim());
        Optional<Category> dbCategoryDeletedFalse = categoryRepo.findCategoryByNameAndDeletedIsFalse(categoryRequest.getName());
        Category category = categoryRequestToEntity(categoryRequest);
        if (dbCategoryDeletedFalse.isPresent()) {
            if(category.getId()!=null){
                if (category.getId().equals(dbCategoryDeletedFalse.get().getId())) {
                    categoryRepo.save(category);
                    return null;
                }
            }
            else {
                return "Category already exist!!";
            }

        }

        Optional<Category> dbCategoryDeletedTrue = categoryRepo.findCategoryByNameAndDeletedIsTrue(categoryRequest.getName());
        if (dbCategoryDeletedTrue.isPresent()) {
            Category dbCategoryCheckTrue = dbCategoryDeletedTrue.get();
            if (dbCategoryCheckTrue.getName().equalsIgnoreCase(category.getName())) {
                dbCategoryCheckTrue.setDescription(category.getDescription());
                dbCategoryCheckTrue.setDeleted(Boolean.FALSE);
                categoryRepo.save(dbCategoryCheckTrue);
                return null;
            }
        }

        categoryRepo.save(category);
        return null;
    }

    @Override
    public String saveUpdateCategory(CategoryRequest categoryRequest) {
        Category category = categoryRequestToEntity(categoryRequest);
        categoryRepo.save(category);
        return "updated";
    }

    //function used to find category & return category id
    public Category findCategoryById(Long id) {
        Optional<Category> singleCategory = categoryRepo.findById(id);
        if (singleCategory.isPresent()) {
            return singleCategory.get();
        }
        throw new CategoryCanNotBeDeletedException("Category having this id does not exist");
    }


    /*
     * converting the entity Category to category Response
     */
    public CategoryResponse entityToCategory(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    /*
     *converting category Request to entity
     */
    public Category categoryRequestToEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setId(categoryRequest.getId());
        category.setName(categoryRequest.getName().trim());
        category.setDescription(categoryRequest.getDescription());
        return category;
    }

    //method used to find all the category present in database & return Categoryresponse dto
    @Override
    public List<CategoryResponse> allCategory() {
        List<Category> categories = categoryRepo.findAll();
        return changeToCategory(categories);
    }


    //this method is used to findCategory by Id & return CategoryResponse
    @Override
    public CategoryResponse findCategoryResponseById(Long id) {
        Optional<Category> singleCategory = categoryRepo.findById(id);
        if (singleCategory.isPresent()) {
            Category category = singleCategory.get();
            return entityToCategory(category);
        }
        throw new CategoryCanNotBeDeletedException("Category having this id does not exist");
    }

    @Override
    public CategoryResponse findCategoryResponseByIdForBook(Long id) {
        Optional<Category> singleCategory = categoryRepo.findById(id);
        if (singleCategory.isPresent()) {
            Category category = singleCategory.get();
            return entityToCategory(category);
        }
        throw new CategoryBookNotFound("Category having this id does not exist");
    }

    //this method is used to delete the category in database using the category ID
    @Override
    public String deleteCategory(Long id) {
        categoryRepo.deleteById(id);
        return "deleted";
    }

    @Override
    public List<CategoryResponse> categoryNotDeleted() {
        List<Category> categories = categoryRepo.categoryNotDeleted();
        return changeToCategory(categories);
    }

    //this method take parameter as Category entity list & return List of CategoryResponse dto
    public List<CategoryResponse> changeToCategory(List<Category> categoryList) {
        return categoryList.stream().map(this::entityToCategory).toList();
    }
}
