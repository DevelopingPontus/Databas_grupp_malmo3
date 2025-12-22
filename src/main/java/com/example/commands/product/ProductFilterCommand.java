package com.example.commands.product;

import com.example.models.Product;
import com.example.services.ProductService;

import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Command(name = "filter", description = "Filter products")
public class ProductFilterCommand implements Runnable{
    private final ProductService productService;

    public ProductFilterCommand(ProductService productService) {
        this.productService = productService;
    }

    @Option(names = {"-s", "--sku"}, description = "List products by SKU")
    private String sku;

    @Option(names = {"-n", "--name"}, description = "List products by name")
    private String name;

    @Option(names = {"-c", "--category"}, description = "List products by category")
    private String categoryName;

    @Override
    public void run() {
        if (sku == null && name == null && categoryName == null) {
            System.out.println("Please specify at least one filter (--sku, --name, or --category)");
            CommandLine.usage(this, System.out);
            return;
        }

        List<Product> products = List.of();
        if (sku != null) {
            products = productService.searchProductBySku(sku)
                    .map(List::of)
                    .orElse(List.of());
        } else if (name != null) {
            products = productService.searchProductByName(name);
        } else if (categoryName != null) {
            products = productService.searchProductByCategory(categoryName);
        }

        if (products.isEmpty()) {
            System.out.println("No products found matching the criteria");
            return;
        }

        System.out.println("Found " + products.size() + " product(s):");
        System.out.println("----------------------------------");
        products.forEach(p -> System.out.printf(
                "ID:%-4d | %-28s | SKU:%-8s | Price:%-8.2f | Category:%-20s | Active:%b%n",
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

