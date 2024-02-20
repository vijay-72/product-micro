package com.example.productservice.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "categories")
@Data
public class Category {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    @Size(min = 3, message = "name must have at least 3 characters")
    private String name;
    private List<String> attributes;




}
