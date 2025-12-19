package com.example.commands.inventory;

import com.example.exceptions.OutOfStockException;
import com.example.services.InventoryService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "update", mixinStandardHelpOptions = true, description = "Update inventory")
public class InventoryUpdateCommand implements Runnable {
    private final InventoryService inventoryService;

    @ArgGroup(exclusive = true, multiplicity = "1")
    private Operation operation;

    @Parameters(index = "0", description = "Product ID")
    private int productId;

    @Parameters(index = "1", description = "Quantity")
    private int quantity;

    public InventoryUpdateCommand(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    static class Operation {
        @Option(names = "increase", description = "Increase stock by specified quantity")
        boolean increase;

        @Option(names = "decrease", description = "Decrease stock by specified quantity")
        boolean decrease;
    }

    @Override
    public void run() {
        if (quantity <= 0) {
            System.err.println("Error: Quantity must be a positive number");
            return;
        }

        try {
            if (operation.increase) {
                inventoryService.increaseStock(productId, quantity);
                System.out.printf("Successfully increased stock for product ID %d by %d%n", productId, quantity);
            } else if (operation.decrease) {
                inventoryService.decreaseStock(productId, quantity);
                System.out.printf("Successfully decreased stock for product ID %d by %d%n", productId, quantity);
            }

            int currentStock = inventoryService.getStock(productId);
            System.out.printf("Current stock level for product ID %d: %d%n", productId, currentStock);

        } catch (OutOfStockException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error updating inventory: " + e.getMessage());
        }
    }
}
