package com.example.respoitories;

import com.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySku(String sku);

    void deleteBySku(String sku);

    List<Product> findProductBySku(String sku);

    List<Product> findProductByName(String name);
}
