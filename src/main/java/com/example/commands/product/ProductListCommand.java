package com.example.commands.product;

import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "list", description = "List all products")
public class ProductListCommand implements Runnable {
    private final ProductService productService;

    public ProductListCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run() {
        System.out.println("Listing all products... ");
        productService.getAllProducts()
                .forEach((p) -> System.out.printf("%s, %s, %s\n", p.getSku(), p.getName(), p.getPrice()));
    }
}
