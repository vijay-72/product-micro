package com.example.productservice.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.util.List;
import java.util.Map;

@Data
@Document("products")
public class Product {
    @Id
    private String id;
    @NotBlank
    @Size(min = 3, message = "Name must be greater than 2 characters")
    private String name;

    private List<Attribute> attributes;

    @DBRef
    private Category category;

    @NotNull(message = "Price must not be null")
    @Min(value = 1, message = "The value must be greater than 0")
    private Integer price;
    @Min(value = 0, message = "The value must be positive")
    @NotNull(message = "Quantity Available must not be null")
    private Integer quantityAvailable;

    private String brand;
    private List<String> images;

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Attribute {
        private String key;
        private String value;
    }
}
