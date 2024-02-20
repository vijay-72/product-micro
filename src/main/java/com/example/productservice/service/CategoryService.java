package com.example.productservice.service;

import com.example.productservice.entity.Category;
import com.example.productservice.exception.GeneralInternalException;
import com.example.productservice.repository.CategoryRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
@Service

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DuplicateKeyException ex) {
            throw new GeneralInternalException("Duplicate name (category)", HttpStatus.BAD_REQUEST);
        }
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName).orElseThrow(() -> new GeneralInternalException("Category name not found", HttpStatus.NOT_FOUND));
    }


}
