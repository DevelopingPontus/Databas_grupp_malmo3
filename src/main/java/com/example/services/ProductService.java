package com.example.services;

import com.example.models.Product;
import com.example.respoitories.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProducts(String sku, String name, String description, double price) {
        Product product = new Product(sku, name, description, price);
        return productRepository.save(product);
    }

    public boolean removeProductByID(int productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    //För att kunna ta bort en produkt med en SKU behövs @Transactional
    //The Transactional annotation ensures that the database operations are performed within a transaction.
    //This is required for any operation that modifies data in the database (like delete, update, etc.) when using JPA/Hibernate
    @Transactional
    public boolean removeProductBySku(String sku) {
        if (productRepository.existsBySku(sku)) {
            productRepository.deleteBySku(sku);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<Product> searchProductBySku(String sku) {
        return productRepository.findProductBySku(sku);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(int productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
}
