package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        String id = productService.addProduct(product, categoryName);
        return ResponseEntity.ok("Added product successfully! Id is: " + id);
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

//    @GetMapping
//    public ResponseEntity<List<Product>> filterProducts(
//            @RequestParam
//    )

    @GetMapping("/search")
    public Page<Product> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size);
        return productService.searchProducts(keyword, category, minPrice, maxPrice, sortBy, pageable);
    }

}
