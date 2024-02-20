package com.example.productservice.service;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.GeneralInternalException;
import com.example.productservice.repository.ProductRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductService {
    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    public ProductService(CategoryService categoryService, ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
    }

    public String addProduct(Product product, String categoryName) {
        try {
            String lowerCaseCategoryName = categoryName.toLowerCase();
            Category category = categoryService.getCategoryByName(lowerCaseCategoryName);
            product.setCategoryName(lowerCaseCategoryName);

            Set<String> requiredAttributes = category.getRequiredAttributes();
            Map<String, String> productAttributes = product.getAttributes();
            if (!productAttributes.keySet().containsAll(requiredAttributes)) {
                throw new GeneralInternalException("Missing required attributes for category: " + category.getName(), HttpStatus.BAD_REQUEST);
            }
            return productRepository.save(product).getId();
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
