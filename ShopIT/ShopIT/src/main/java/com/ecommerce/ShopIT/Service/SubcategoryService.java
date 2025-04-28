package com.ecommerce.ShopIT.Service;



import com.ecommerce.ShopIT.Entity.Category;
import com.ecommerce.ShopIT.Entity.Subcategory;
import com.ecommerce.ShopIT.Entity.SubcategoryDTO;
import com.ecommerce.ShopIT.Repository.CategoryRepository;
import com.ecommerce.ShopIT.Repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Subcategory createSubcategory(SubcategoryDTO subcategoryDTO) {
        Optional<Category> categoryOpt = categoryRepository.findById(subcategoryDTO.getCategoryId());
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found with id: " + subcategoryDTO.getCategoryId());
        }

        Subcategory subcategory = new Subcategory(subcategoryDTO.getName(), categoryOpt.get());
        return subcategoryRepository.save(subcategory);
    }

    public List<Subcategory> createMultipleSubcategories(List<SubcategoryDTO> subcategoryDTOs) {
        List<Subcategory> createdSubcategories = new ArrayList<>();

        for (SubcategoryDTO dto : subcategoryDTOs) {
            Optional<Category> categoryOpt = categoryRepository.findById(dto.getCategoryId());
            if (categoryOpt.isEmpty()) {
                throw new RuntimeException("Category not found with id: " + dto.getCategoryId());
            }

            Subcategory subcategory = new Subcategory(dto.getName(), categoryOpt.get());
            createdSubcategories.add(subcategoryRepository.save(subcategory));
        }

        return createdSubcategories;
    }

    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    public Optional<Subcategory> getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id);
    }

    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId);
    }

    public Subcategory updateSubcategory(Long id, SubcategoryDTO subcategoryDTO) {
        Optional<Subcategory> existingSubcategoryOpt = subcategoryRepository.findById(id);
        if (existingSubcategoryOpt.isEmpty()) {
            throw new RuntimeException("Subcategory not found with id: " + id);
        }

        Optional<Category> categoryOpt = categoryRepository.findById(subcategoryDTO.getCategoryId());
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Category not found with id: " + subcategoryDTO.getCategoryId());
        }

        Subcategory existingSubcategory = existingSubcategoryOpt.get();
        existingSubcategory.setName(subcategoryDTO.getName());
        existingSubcategory.setCategory(categoryOpt.get());

        return subcategoryRepository.save(existingSubcategory);
    }

    public void deleteSubcategory(Long id) {
        if (!subcategoryRepository.existsById(id)) {
            throw new RuntimeException("Subcategory not found with id: " + id);
        }
        subcategoryRepository.deleteById(id);
    }
}
