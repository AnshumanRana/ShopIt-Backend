package com.ecommerce.ShopIT.Repository;

import com.ecommerce.ShopIT.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySubcategoryId(Long subcategoryId);
}