package com.ecommerce.ShopIT.Entity;

import lombok.Data;
import java.util.List;

@Data
public class MultipleSubcategoryDTO {
    private Long categoryId;
    private List<String> subcategoryNames;

    // Constructors
    public MultipleSubcategoryDTO() {
    }

    public MultipleSubcategoryDTO(Long categoryId, List<String> subcategoryNames) {
        this.categoryId = categoryId;
        this.subcategoryNames = subcategoryNames;
    }
}