package com.ecommerce.ShopIT.Controller;

import com.ecommerce.ShopIT.Entity.Category;
import com.ecommerce.ShopIT.Entity.MultipleSubcategoryDTO;
import com.ecommerce.ShopIT.Entity.Subcategory;
import com.ecommerce.ShopIT.Entity.SubcategoryDTO;
import com.ecommerce.ShopIT.Repository.CategoryRepository;
import com.ecommerce.ShopIT.Repository.SubcategoryRepository;
import com.ecommerce.ShopIT.Service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin(origins = "*")
public class SubcategoryController {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryService subcategoryService;

    // Create a new subcategory
    @PostMapping("")
    public ResponseEntity<?> createSubcategory(@RequestBody Map<String, Object> payload) {
        try {
            String name = (String) payload.get("name");
            Long categoryId = Long.valueOf(payload.get("categoryId").toString());

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Subcategory name is required");
            }

            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Category not found");
            }

            Subcategory subcategory = new Subcategory(name, categoryOpt.get());
            Subcategory savedSubcategory = subcategoryRepository.save(subcategory);
            return ResponseEntity.ok(savedSubcategory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error creating subcategory: " + e.getMessage());
        }
    }

    // Create multiple subcategories for a single category
    @PostMapping("/multiple")
    public ResponseEntity<?> createMultipleSubcategories(@RequestBody MultipleSubcategoryDTO multipleSubcategoryDTO) {
        try {
            if (multipleSubcategoryDTO.getCategoryId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Category ID is required");
            }

            if (multipleSubcategoryDTO.getSubcategoryNames() == null || multipleSubcategoryDTO.getSubcategoryNames().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("At least one subcategory name is required");
            }

            Optional<Category> categoryOpt = categoryRepository.findById(multipleSubcategoryDTO.getCategoryId());
            if (categoryOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Category not found");
            }

            List<SubcategoryDTO> subcategoryDTOs = new ArrayList<>();
            for (String name : multipleSubcategoryDTO.getSubcategoryNames()) {
                if (name != null && !name.trim().isEmpty()) {
                    subcategoryDTOs.add(new SubcategoryDTO(name, multipleSubcategoryDTO.getCategoryId()));
                }
            }

            List<Subcategory> savedSubcategories = subcategoryService.createMultipleSubcategories(subcategoryDTOs);
            return ResponseEntity.ok(savedSubcategories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error creating subcategories: " + e.getMessage());
        }
    }

    // Create multiple subcategories using a list of DTOs
    @PostMapping("/batch")
    public ResponseEntity<?> createBatchSubcategories(@RequestBody List<SubcategoryDTO> subcategoryDTOs) {
        try {
            if (subcategoryDTOs == null || subcategoryDTOs.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("At least one subcategory is required");
            }

            List<Subcategory> savedSubcategories = subcategoryService.createMultipleSubcategories(subcategoryDTOs);
            return ResponseEntity.ok(savedSubcategories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error creating subcategories: " + e.getMessage());
        }
    }

    // Get all subcategories
    @GetMapping("")
    public ResponseEntity<List<Subcategory>> getAllSubcategories() {
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        return ResponseEntity.ok(subcategories);
    }

    // Get subcategories by category ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Subcategory>> getSubcategoriesByCategoryId(@PathVariable Long categoryId) {
        List<Subcategory> subcategories = subcategoryRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(subcategories);
    }

    // Get subcategory by ID
    @GetMapping("/{id}")
    public ResponseEntity<Subcategory> getSubcategoryById(@PathVariable Long id) {
        Optional<Subcategory> subcategory = subcategoryRepository.findById(id);
        return subcategory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update subcategory
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubcategory(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            String name = (String) payload.get("name");
            Long categoryId = Long.valueOf(payload.get("categoryId").toString());

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Subcategory name is required");
            }

            Optional<Subcategory> existingSubcategoryOpt = subcategoryRepository.findById(id);
            if (existingSubcategoryOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Category not found");
            }

            Subcategory existingSubcategory = existingSubcategoryOpt.get();
            existingSubcategory.setName(name);
            existingSubcategory.setCategory(categoryOpt.get());

            Subcategory updatedSubcategory = subcategoryRepository.save(existingSubcategory);
            return ResponseEntity.ok(updatedSubcategory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error updating subcategory: " + e.getMessage());
        }
    }

    // Delete subcategory
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        if (!subcategoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        subcategoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}