package com.example.commands.product;

import com.example.models.Category;
import com.example.models.Product;
import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.List;

@Component
@Command(name = "list", description = "List all products")
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
    private Category categories;

    @Override
    public void run() {
        List<Product> products;
        if (sku != null) {
            products = productService.searchProductBySku(sku);
        } else if (name != null) {
            products = productService.searchProductByName(name);
        } else if (categories != null) {
            products = productService.searchProductByCategory(categories);
        } else {
            products = productService.getAllProducts();
        }

        // Display results
        if (products.isEmpty()) {
            System.out.println("No products found");
            return;
        }

        System.out.println("Listing all products... ");
        products.forEach((p) -> {
            String categories = p.getCategories().isEmpty() ? "No categories" : 
                    p.getCategories().stream()
                            .map(category -> category.getName())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("");
            
            System.out.printf("- %s (SKU: %s, Price: %.2f, Description: %s, Active: %b, Categories: %s)%n",
                    p.getName(), p.getSku(), p.getPrice(), p.getDescription(), p.isActive(), categories);
        });

    }
}
