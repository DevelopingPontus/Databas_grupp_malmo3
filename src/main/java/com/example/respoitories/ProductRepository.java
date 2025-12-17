package com.example.respoitories;

import com.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySku(String sku);

    void deleteBySku(String sku);
}
