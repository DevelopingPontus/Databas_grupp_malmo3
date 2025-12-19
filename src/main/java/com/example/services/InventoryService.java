package com.example.services;

import com.example.exceptions.OutOfStockException;
import com.example.models.Inventory;
import com.example.models.OrderItem;
import com.example.models.Orders;
import com.example.respoitories.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }

    public void reserve(Orders order) {
        for (OrderItem item : order.getOrderItems()) {
            int inStock = getStock(item.getProduct().getId());
            if (inStock < item.getQuantity()) {
                throw new OutOfStockException(
                        item.getProduct().getName() + " only has " + inStock + " left"
                );

            }

            decreaseStock(item.getProduct().getId(), item.getQuantity());
        }
    }

    public void release(Orders order) {
        for (OrderItem item : order.getOrderItems()) {
            increaseStock(item.getProduct().getId(), item.getQuantity());
        }
    }

    //--helper methods
    public int getStock(int productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalStateException("No inventory found for product " + productId))
                .getQuantity();
    }

    public void decreaseStock(int productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalStateException("No inventory found for product " + productId));
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    public void increaseStock(int productId, int quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalStateException("No inventory found for product " + productId));
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    // list

    // update

}