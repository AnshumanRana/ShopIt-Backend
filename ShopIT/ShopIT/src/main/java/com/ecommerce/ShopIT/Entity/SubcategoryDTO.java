package com.ecommerce.ShopIT.Entity;

import lombok.Data;

@Data
public class SubcategoryDTO {

    private String name;
    private Long categoryId;

    // Constructors
    public SubcategoryDTO() {
    }

    public SubcategoryDTO(String name, Long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }


}
