package com.ecommerce.ShopIT.Service;

import com.ecommerce.ShopIT.Entity.Product;
import com.ecommerce.ShopIT.Entity.Subcategory;
import com.ecommerce.ShopIT.Repository.ProductRepository;
import com.ecommerce.ShopIT.Repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsBySubcategoryId(Long subcategoryId) {
        return productRepository.findBySubcategoryId(subcategoryId);
    }

    public List<Product> getProductsBySubcategoryName(String subcategoryName) {
        Optional<Subcategory> subcategory = subcategoryRepository.findByName(subcategoryName);

        if (subcategory.isEmpty()) {
            return List.of(); // Return empty list if subcategory not found
        }

        return productRepository.findBySubcategoryId(subcategory.get().getId());
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setImageUrl(updatedProduct.getImageUrl());
            product.setSubcategory(updatedProduct.getSubcategory());
            return productRepository.save(product);
        }).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}