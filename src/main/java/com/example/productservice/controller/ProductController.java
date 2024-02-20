package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("add-product/{categoryName}")
    public ResponseEntity<String> addProduct(@Valid @RequestBody Product product, @NotBlank @PathVariable String categoryName ) {
        Product addedProd = productService.addProduct(product, categoryName);
        return ResponseEntity.ok("Added product successfully! Id is: " + addedProd.getId());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    @DeleteMapping("/remove-product/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable @NotBlank String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Deleted successfully!");
    }

}
