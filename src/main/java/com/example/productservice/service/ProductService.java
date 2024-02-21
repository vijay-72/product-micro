package com.example.productservice.service;

import com.example.productservice.entity.Category;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.GeneralInternalException;
import com.example.productservice.repository.ProductRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProductService {
    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    private final MongoTemplate mongoTemplate;

    public ProductService(CategoryService categoryService, ProductRepository productRepository, MongoTemplate mongoTemplate) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
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
        if (productRepository.deleteProductById(id) == 0) throw new GeneralInternalException("Something went wrong when deleting product with id: " + id,
                HttpStatus.NOT_FOUND);
    }

    public Page<Product> searchProducts(String keyword, String category, Double minPrice, Double maxPrice, String sortBy, Pageable pageable) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("name").regex(keyword, "i"),
                    Criteria.where("brand").regex(keyword, "i"),
                    Criteria.where("categoryName").regex(keyword, "i"),
                    Criteria.where("description").regex(keyword, "i")
            ));
        }

        if (category != null && !category.isEmpty()) {
            criteriaList.add(Criteria.where("categoryName").is(category));
        }

        if (minPrice != null) {
            criteriaList.add(Criteria.where("price").gte(minPrice));
        }

        if (maxPrice != null) {
            criteriaList.add(Criteria.where("price").lte(maxPrice));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            query.with(org.springframework.data.domain.Sort.by(sortBy));
        }

        long totalCount = mongoTemplate.count(query, Product.class);

        query.with(pageable);

        List<Product> productList = mongoTemplate.find(query, Product.class);

        return new PageImpl<>(productList, pageable, totalCount);
    }
}
