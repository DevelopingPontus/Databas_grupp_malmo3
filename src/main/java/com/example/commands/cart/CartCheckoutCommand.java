package com.example.commands.cart;

import com.example.models.Payment;
import com.example.services.CartService;
import com.example.exceptions.OutOfStockException;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "checkout", mixinStandardHelpOptions = true, description = "Checkout cart")
public class CartCheckoutCommand implements Runnable {

    private final CartService cartService;

    @Parameters(index = "0", description = "Customer id")
    private int customerId;

    @Parameters(index = "1", description = "Payment method (CARD|INVOICE)")
    private Payment.PaymentMethod method;

    public CartCheckoutCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void run() {
        try {
            cartService.checkout(customerId, method);
            System.out.println("Order completed successfully");

        } catch (OutOfStockException e) {
            System.out.println("Out of stock: " + e.getMessage());

        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Checkout failed");
        }
    }
}
