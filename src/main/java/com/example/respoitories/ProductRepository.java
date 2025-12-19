package com.example.respoitories;

import com.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySku(String sku);

    void deleteBySku(String sku);

    List<Product> findProductBySku(String sku);

    List<Product> findProductByName(String name);

    @Query("""
    SELECT DISTINCT p
    FROM Product p
    JOIN FETCH p.categories c
    WHERE c.name = :categoryName
    """)
    List<Product> findProductByCategory(String categoryName);
}
