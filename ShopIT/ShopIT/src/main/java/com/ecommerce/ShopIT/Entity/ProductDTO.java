package com.ecommerce.ShopIT.Entity;

import lombok.Data;

@Data
public class ProductDTO {

    private String name;
    private String description;
    private Double price;
    private Long subcategoryId;

    // Constructors
    public ProductDTO() {
    }

    public ProductDTO(String name, String description, Double price, Long subcategoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.subcategoryId = subcategoryId;
    }



}