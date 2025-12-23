package com.example.commands.product;

import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.math.BigDecimal;

@Component
@Command(name = "add", mixinStandardHelpOptions = true, description = "Add a new product")
public class ProductAddCommand implements Runnable {
    private final ProductService productService;

    @Parameters(index = "0", description = "Product SKU")
    private String sku;

    @Parameters(index = "1", description = "Name to product")
    private String name;

    @Parameters(index = "2", description = "Product price")
    private BigDecimal price;

    @Parameters(index = "3", description = "Product description", arity = "0..1")
    private String description;

    public ProductAddCommand(ProductService productService) {
        this.productService = productService;
    }

    public void run() {
        var product = productService.addProducts(sku, name, description, price);
        System.out.printf(" SKU:%s | Name:%s | Price:%.2f | Description:%s | Active:%b%n",
                product.getSku().toLowerCase(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.isActive()
        );
    }
}
