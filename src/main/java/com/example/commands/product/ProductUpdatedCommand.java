package com.example.commands.product;

import com.example.models.Product;
import com.example.services.ProductService;
import org.springframework.stereotype.Component;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Command(name = "update", description = "Update a product")
public class ProductUpdatedCommand implements Runnable {
    private final ProductService productService;

    public ProductUpdatedCommand(ProductService productService) {
        this.productService = productService;
    }

    @Parameters(index = "0", description = "To update product, enter SKU:")
    private String sku;

    @Option(names = {"-n", "--name"}, description = "Update product name")
    private String newName;

    @Option(names = {"-s", "--sku"}, description = "Update SKU (unique)")
    private String newSku;

    @Option(names = {"-d", "--description"}, description = "Update description")
    private String newDescription;

    @Option(names = {"-p", "--price"}, description = "Update price")
    private Double newPrice;

    @Option(names = {"-a", "--active"}, description = "Update product status (True/False)")
    private Boolean active;

    @Override
    public void run() {
        System.out.printf("Updating product with SKU: %s\n", sku);
        Optional<Product> existingProduct = productService.searchProductBySku(sku);

        if (existingProduct.isEmpty()) {
            System.out.println("Error: Product not found with SKU: " + sku);
            return;
        }

        Product product = existingProduct.get();
        boolean updated = false;

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
            System.out.println("----------------------------------");
            System.out.printf("Name:%s | SKU:%s | Description:%s | Price:%.2f | Category:%s | Active:%b%n",
                    product.getName(),
                    product.getSku().toLowerCase(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategories().isEmpty() ? "None" : product.getCategories().stream()
                            .map(c -> c.getName())
                            .collect(Collectors.joining(", ")),
                    product.isActive()
            );
        } catch (Exception e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }
}
