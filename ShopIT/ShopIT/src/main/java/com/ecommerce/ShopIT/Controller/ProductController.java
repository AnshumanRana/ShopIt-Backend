package com.ecommerce.ShopIT.Controller;

import com.ecommerce.ShopIT.Entity.Product;
import com.ecommerce.ShopIT.Entity.ProductDTO;
import com.ecommerce.ShopIT.Entity.Subcategory;
import com.ecommerce.ShopIT.Repository.ProductRepository;
import com.ecommerce.ShopIT.Repository.SubcategoryRepository;
import com.ecommerce.ShopIT.Service.ImageUploadService;
import com.ecommerce.ShopIT.Service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private ProductService productService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductDTO dto = mapper.readValue(productJson, ProductDTO.class);

            // Find the subcategory
            Optional<Subcategory> subcategoryOpt = subcategoryRepository.findById(dto.getSubcategoryId());
            if (subcategoryOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Subcategory not found");
            }

            String imageUrl = "";
            if (image != null && !image.isEmpty()) {
                imageUrl = imageUploadService.uploadImage(image);
            }

            Product product = new Product(dto.getName(), dto.getDescription(), dto.getPrice(), imageUrl, subcategoryOpt.get());
            Product saved = productRepository.save(product);

            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error creating product: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<List<Product>> getProductsBySubcategoryId(@PathVariable Long subcategoryId) {
        List<Product> products = productService.getProductsBySubcategoryId(subcategoryId);
        return ResponseEntity.ok(products);
    }

    // Endpoint to get products by subcategory name
    @GetMapping("/subcategory")
    public ResponseEntity<List<Product>> getProductsBySubcategoryName(@RequestParam("name") String subcategoryName) {
        List<Product> products = productService.getProductsBySubcategoryName(subcategoryName);
        return ResponseEntity.ok(products);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (existingProductOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            ObjectMapper mapper = new ObjectMapper();
            ProductDTO dto = mapper.readValue(productJson, ProductDTO.class);

            // Find the subcategory
            Optional<Subcategory> subcategoryOpt = subcategoryRepository.findById(dto.getSubcategoryId());
            if (subcategoryOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Subcategory not found");
            }

            Product existingProduct = existingProductOpt.get();

            existingProduct.setName(dto.getName());
            existingProduct.setDescription(dto.getDescription());
            existingProduct.setPrice(dto.getPrice());
            existingProduct.setSubcategory(subcategoryOpt.get());

            if (image != null && !image.isEmpty()) {
                String imageUrl = imageUploadService.uploadImage(image);
                existingProduct.setImageUrl(imageUrl);
            }

            Product updated = productRepository.save(existingProduct);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error updating product: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}