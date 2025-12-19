package com.example.commands.product;

import com.example.models.Product;
import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Command(name = "list", description = "List all products")
@Transactional
public class ProductListCommand implements Runnable {
    private final ProductService productService;

    public ProductListCommand(ProductService productService) {
        this.productService = productService;
    }

    @Option(names = "--sku", description = "List products by SKU")
    private String sku;

    @Option(names = "--name", description = "List products by name")
    private String name;

    @Option(names = "--category", description = "List products by category")
    private String categoryName;

    @Override
    public void run() {
        List<Product> products;
        if (sku != null) {
            products = productService.searchProductBySku(sku);
        } else if (name != null) {
            products = productService.searchProductByName(name);
        } else if (categoryName != null) {
            products = productService.searchProductByCategory(categoryName);
        } else {
            products = productService.getAllProducts();
        }

        if (products.isEmpty()) {
            System.out.println("No products found");
            return;
        }

        System.out.println("Listing products...");
        products.forEach(product -> {
            String categoryName = "No category";
            if (!product.getCategories().isEmpty()) {
                categoryName = product.getCategories().iterator().next().getName();
            }

            System.out.println(
                    "ID: " + product.getId() +
                    ", Name: " + product.getName()
                    + ", SKU: " + product.getSku()
                    + ", Price: " + product.getPrice()
                    + ", Description: " + product.getDescription()
                    + ", Active: " + product.isActive()
                    + ", Category: " + categoryName
            );
        });
    }
}
