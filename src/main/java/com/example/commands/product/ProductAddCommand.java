package com.example.commands.product;

import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Component
@Command(name = "add", description = "Add a new product")
public class ProductAddCommand implements Runnable {
    private final ProductService productService;

    //String sku, String name, String description, double price
    @Parameters(index = "0", description = "Product SKU")
    private String sku;

    @Parameters(index = "1", description = "Name to product")
    private String name;

    @Parameters(index = "2", description = "Product price")
    private double price;

    @Parameters(index = "3", description = "Product description", arity = "0..1")
    private String description;

    public ProductAddCommand(ProductService productService) {
        this.productService = productService;
    }

    public void run() {
        var product = productService.addProducts(sku, name, description, price);
        System.out.println("SKU:" + product.getSku() + " Name:" + product.getName() +
                " Description:" + product.getDescription() + " Price:" + product.getPrice());
    }
}
