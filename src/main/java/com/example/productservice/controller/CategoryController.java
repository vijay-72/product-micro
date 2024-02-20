package com.example.productservice.controller;

import com.example.productservice.entity.Category;
import com.example.productservice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add-category")
    public ResponseEntity<String> addCategory(@Valid @RequestBody Category category) {
        Category addedCategory = categoryService.addCategory(category);
        return ResponseEntity.ok("Added category successfully! Id is: " + addedCategory.getId());
    }
}
