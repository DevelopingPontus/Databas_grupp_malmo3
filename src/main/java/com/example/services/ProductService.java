package com.example.services;

import com.example.models.Product;
import com.example.repositories.ProductRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProducts(String sku, String name, String description, BigDecimal price) {
        if (sku != null) {
            sku = sku.toUpperCase();
        }
        Product product = new Product(sku, name, description, price);
        return productRepository.save(product);
    }

    @Transactional
    public boolean removeProductBySku(String sku) {
        if (sku == null) return false;
        int deleted = productRepository.deleteBySku(sku);
        return deleted > 0;
    }

    @Transactional
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> searchProductBySku(String sku) {
        if (sku == null) return Optional.empty();
        return productRepository.findProductBySku(sku);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Transactional
    public List<Product> searchProductByCategory(String categoryName) {
        return productRepository.findProductByCategory(categoryName);
    }

    @Transactional
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
}
