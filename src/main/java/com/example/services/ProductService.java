package com.example.services;

import com.example.models.Product;
import com.example.respoitories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean removeProductBySku(String sku) {
        if (productRepository.existsBySku(sku)) {
            productRepository.deleteBySku(sku);
            return true;
        }
        return false;
    }

}
