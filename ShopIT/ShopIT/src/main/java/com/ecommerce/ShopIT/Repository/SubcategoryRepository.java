package com.ecommerce.ShopIT.Repository;

import com.ecommerce.ShopIT.Entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Optional<Subcategory> findByName(String name);
    List<Subcategory> findByCategoryId(Long categoryId);
}