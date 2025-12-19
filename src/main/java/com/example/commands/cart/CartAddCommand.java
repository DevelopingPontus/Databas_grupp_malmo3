package com.example.commands.cart;

import com.example.exceptions.OutOfStockException;
import com.example.services.CartService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.NoSuchElementException;

@Component
@Command(name = "add", mixinStandardHelpOptions = true, description = "Add a new cart")
public class CartAddCommand implements Runnable {
    private final CartService cartService;

    @Parameters(index = "0", description = "The customer email")
    private String customerEmail;

    @Parameters(index = "1", description = "The product SKU")
    private String productSku;

    @Parameters(index = "2", description = "The quantity")
    private int quantity;

    public CartAddCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run() {
        try {
            validateInput();
            cartService.addToCart(customerEmail, productSku, quantity);

            System.out.printf(
                    "Added product %s (qty %d) to cart for customer %s%n",
                    productSku, quantity, customerEmail);

        } catch (OutOfStockException e) {
            System.out.println("Not enough stock for product: " + productSku);
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
