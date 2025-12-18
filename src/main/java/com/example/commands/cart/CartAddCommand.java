package com.example.commands.cart;

import com.example.services.CartService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "add", description = "Add a new cart")
public class CartAddCommand implements Runnable {
    private final CartService cartService;

    @Parameters(index = "0", description = "The customer id")
    private int CustomerId;

    @Parameters(index = "1", description = "The product id")
    private int productId;

    @Parameters(index = "2", description = "The quantity")
    private int quantity;

    public CartAddCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run() {
//        var cart = cartService.addToCart(CustomerId, productId, quantity);
//        System.out.println("Added to cart for customer : " + cart);

    }
}
