package com.example.commands.product;

import com.example.models.Product;
import com.example.respoitories.ProductRepository;
import com.example.services.ProductService;
import org.springframework.stereotype.Component;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Command(name = "update", description = "Update a product")
public class ProductUpdatedCommand implements Runnable {
    private final ProductService productService;

    public ProductUpdatedCommand(ProductService productService) {
        this.productService = productService;
    }

    //TODO ska endast kunnas sökas på den specifika produkt via --sku inte id
    @Parameters(index = "0", description = "Enter ID of product to update")
    private int productId;

    @Option(names = "--name", description = "New product name")
    private String newName;

    @Option(names = "--sku", description = "New SKU (must be unique)")
    private String newSku;

    @Option(names = "--description", description = "New description")
    private String newDescription;

    @Option(names = "--price", description = "New price")
    private Double newPrice;

    @Option(names = "--active", description = "Set product active status (true/false)")
    private Boolean active;

    @Override
    public void run() {
        System.out.printf("Updating product with ID: %d\n", productId);

        // First, get the existing product
        Optional<Product> existingProduct = productService.findById(productId);

        if (existingProduct.isEmpty()) {
            System.out.println("Error: Product not found with ID: " + productId);
            return;
        }

        Product product = existingProduct.get();
        boolean updated = false;

        // Update fields if they are provided
        if (newName != null && !newName.isBlank()) {
            product.setName(newName);
            updated = true;
        }
        if (newSku != null && !newSku.isBlank()) {
            product.setSku(newSku);
            updated = true;
        }
        if (newDescription != null) {
            product.setDescription(newDescription);
            updated = true;
        }
        if (newPrice != null) {
            product.setPrice(new BigDecimal(newPrice));
            updated = true;
        }
        if (active != null) {
            product.setActive(active);
            updated = true;
        }

        if (!updated) {
            System.out.println("No updates provided. Please specify at least one field to update.");
            return;
        }

        try {
            productService.updateProduct(product);
            System.out.println("Product updated successfully:");
            System.out.printf("- ID: %d%n", product.getId());
            System.out.printf("- Name: %s%n", product.getName());
            System.out.printf("- SKU: %s%n", product.getSku());
            System.out.printf("- Description: %s%n", product.getDescription());
            System.out.printf("- Price: %.2f%n", product.getPrice());
            System.out.printf("- Active: %s%n", product.isActive());
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }
}
