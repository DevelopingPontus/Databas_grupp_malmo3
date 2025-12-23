package com.example.commands.product;

import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "remove", mixinStandardHelpOptions = true, description = "remove product")
public class ProductRemoveCommand implements Runnable {
    private final ProductService productService;

    public ProductRemoveCommand(ProductService productService) {
        this.productService = productService;
    }

    @Parameters(index = "0", description = "To remove product enter SKU: ", arity = "0..1")
    private String sku;

    @Override
    public void run() {
        if (sku != null) {
            System.out.printf("Removing product with SKU: %s\n", sku);
            boolean removed = productService.removeProductBySku(sku);
            if (removed) {
                System.out.println("Product removed successfully");
            } else {
                System.out.println("Product not found");
            }
        }
    }
}
