package com.example.commands.cart;

import com.example.services.CartService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.NoSuchElementException;

@Component
@Command(name = "add", description = "Add a new cart")
public class CartAddCommand implements Runnable {
    private final CartService cartService;

    @Parameters(index = "0", description = "The customer id")
    private int customerId;

    @Parameters(index = "1", description = "The product id")
    private int productId;

    @Parameters(index = "2", description = "The quantity")
    private int quantity;

    public CartAddCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run() {
        try {
            validateInput();
            cartService.addToCart(customerId, productId, quantity);

            System.out.printf(
                    "Added product %d (qty %d) to cart for customer %d%n",
                    productId, quantity, customerId
            );

        } catch (NoSuchElementException e) {
            System.out.println("Customer or product not found");

        } catch (Exception e) {
            System.out.println("Could not add product to cart: " + e.getMessage());
        }

    }

    private void validateInput() {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}
