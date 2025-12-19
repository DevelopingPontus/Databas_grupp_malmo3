package com.example.commands.inventory;

import com.example.services.InventoryService;
import com.example.services.ProductService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "list", mixinStandardHelpOptions = true, description = "List inventory items")
public class InventoryListCommand implements Runnable {
    private final InventoryService inventoryService;
    private final ProductService productService;

    public InventoryListCommand(InventoryService inventoryService, ProductService productService){
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @Override
    public void run(){
        System.out.println("Listing all products and stock");
        var products = productService.getAllProducts();

        if(products.isEmpty()){
            System.out.println("No products found");
        }

        for (var product : products) {
            int stock = 0;
            var inventory = product.getInventory();
            if (inventory != null) {
                stock = inventory.getQuantity();
            }


            System.out.printf("%-5s %-10s %-30s %-10d %-15.2f%n",
                    product.getId(),
                    product.getSku(),
                    product.getName(),
                    stock,
                    product.getPrice().doubleValue());

        }
    }
}
