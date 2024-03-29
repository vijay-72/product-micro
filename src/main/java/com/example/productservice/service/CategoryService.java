package com.example.productservice.service;

import com.example.productservice.entity.Category;
import com.example.productservice.exception.GeneralInternalException;
import com.example.productservice.repository.CategoryRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category) {
        String categoryName = category.getName().toLowerCase();
        category.setName(categoryName);        
        try {
            return categoryRepository.save(category);
        } catch (DuplicateKeyException ex) {
            throw new GeneralInternalException("Duplicate name (category)", HttpStatus.BAD_REQUEST);
        } catch (DataAccessException ex) {
            throw new GeneralInternalException("Database error while adding category");
        }
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new GeneralInternalException("Category name not found", HttpStatus.NOT_FOUND));
    }


    public List<Category> getAllProducts() {
        try {
            return categoryRepository.findAll();
        } catch (DataAccessException ex) {
            throw new GeneralInternalException("Some database error while fetching all products");
        }
    }

//    public ResponseEntity<Category> getCategoryDetails(String categoryName) {
//        try {
//            return categoryRepository.findByName(categoryName).orElseThrow(());
//        } catch (DataAccessException ex) {
//            throw new GeneralInternalException("Some database error while fetching all products");
//        }
//    }
}
