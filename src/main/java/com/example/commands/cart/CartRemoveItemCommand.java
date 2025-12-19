package com.example.commands.cart;

import com.example.services.CartService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "remove", mixinStandardHelpOptions = true, description = "Remove item from cart")
public class CartRemoveItemCommand implements Runnable {
    private final CartService cartService;

    @Parameters(index = "0", description = "The customer email")
    private String customerEmail;

    @Parameters(index = "1", description = "The product SKU")
    private String productSku;

    public CartRemoveItemCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run() {
        try {
            cartService.removeFromCart(customerEmail, productSku);
            System.out.println("Product removed from cart");

        } catch (Exception e) {
            System.out.println("Could not remove product from cart");
        }
    }
}
