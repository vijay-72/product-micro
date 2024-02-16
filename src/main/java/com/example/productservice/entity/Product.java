package com.example.productservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document("products")
public class Product {
    @Id
    private String id;
    private String name;

    private List<Map<String, String>> attributes;

    private String categoryId;

    private Integer price;
    private Integer quantityAvailable;

    private String brand;

}
