package com.example.commands.cart;

import com.example.models.Orders;
import com.example.services.CartService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "list", mixinStandardHelpOptions = true, description = "List cart items")
public class CartListCommand implements Runnable {

    private final CartService cartService;

    @Parameters(index = "0", description = "The customer email")
    private String customerEmail;

    public CartListCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run() {
        try {
            Orders cart = cartService.getOrder(customerEmail);

            if (cart.getOrderItems().isEmpty()) {
                System.out.println("Cart is empty");
                return;
            }

            System.out.println("Cart contents:");
            System.out.println("--------------------------------");

            cart.getOrderItems().forEach(item -> {
                System.out.printf(
                        "%s (id:%d) | qty: %d | unit: %.2f | total: %.2f%n",
                        item.getProduct().getName(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal());
            });

            System.out.println("--------------------------------");
            System.out.printf("Total: %.2f%n", cart.getTotal());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
