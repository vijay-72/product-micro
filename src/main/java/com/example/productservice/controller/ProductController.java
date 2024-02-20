package com.example.productservice.controller;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Product> getProducts() {
        return null;
    }

    @PostMapping("/products/{categoryId}")
    public ResponseEntity<String> addProduct(@Valid @RequestBody Product product, @NotBlank @PathVariable String categoryId ) {
        Product addedProd = productService.addProduct(product, categoryId);
        return ResponseEntity.ok("Added product successfully! Id is: " + addedProd.getId());
    }

    @PostMapping("/category")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category) {
        try {
            return new ResponseEntity<>(productService.addCategory(category), HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
