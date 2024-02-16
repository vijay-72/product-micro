package com.example.productservice.entity;

import org.springframework.data.annotation.Id;
import java.util.List;
import java.util.Map;
public class Category {
    @Id
    private String id;
    private String name;
    private List<Map<String, String>> attributes;




}
