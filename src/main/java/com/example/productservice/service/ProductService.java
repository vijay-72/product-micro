package com.example.productservice.service;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.GeneralInternalException;
import com.example.productservice.repository.ProductRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    public ProductService(CategoryService categoryService, ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product, String categoryName) {
        try {
            Category category = categoryService.getCategoryByName(categoryName);
            product.setCategory(category);
            return productRepository.save(product);
        } catch (DataAccessException ex) {
            throw new GeneralInternalException("Some database error while adding product");
        }
    }

    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (DataAccessException ex) {
            throw new GeneralInternalException("Some database error while fetching all products");
        }
    }

    public void deleteProduct(String id) {
        if (productRepository.deleteProductById(id) == 0) throw new GeneralInternalException("Something went wrong when deleting product with id" + id);
    }
}
