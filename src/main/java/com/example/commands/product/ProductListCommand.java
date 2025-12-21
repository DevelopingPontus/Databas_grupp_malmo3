package com.example.commands.product;

import com.example.models.Product;
import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@Command(name = "list", mixinStandardHelpOptions = true, description = "List all products")
public class ProductListCommand implements Runnable {
    private final ProductService productService;

    public ProductListCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run() {
        //TODO if no product is found, return "No products found"
        List<Product> products;
        products = productService.getAllProducts();

        System.out.println("Listing products...");
        System.out.println("----------------------------------");
        products.forEach(p -> System.out.printf(
                "ID:%-4d | %-25s | SKU:%-8s | Price:%-8.2f | Category:%-18s | Active:%b%n",
                p.getId(),
                p.getName(),
                p.getSku().toLowerCase(),
                p.getPrice(),
                p.getCategories().isEmpty() ? "None" : p.getCategories().stream()
                        .map(c -> c.getName())
                        .collect(Collectors.joining(", ")),
                p.isActive()
        ));
    }
}
