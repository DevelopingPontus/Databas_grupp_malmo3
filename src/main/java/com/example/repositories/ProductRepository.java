package com.example.repositories;

import com.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Modifying
    @Query("DELETE FROM Product p WHERE UPPER(p.sku) = UPPER(?1)")
    @Transactional
    int deleteBySku(String sku);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.categories WHERE UPPER(p.sku) = UPPER(:sku)")
    Optional<Product> findProductBySku(@Param("sku") String sku);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.categories WHERE LOWER(p.name) LIKE LOWER(concat('%', :name, '%'))")
    List<Product> findProductByName(@Param("name") String name);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.categories c WHERE c.name = :categoryName")
    List<Product> findProductByCategory(String categoryName);
}
