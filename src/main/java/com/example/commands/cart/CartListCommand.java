package com.example.commands.cart;

import com.example.models.Orders;
import com.example.services.CartService;
import com.example.services.CustomerService;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "list", mixinStandardHelpOptions = true, description = "List cart items")
public class CartListCommand implements Runnable {

    private final CartService cartService;
    private final CustomerService customerService;

    @Parameters(index = "0", description = "The customer email")
    private String customerEmail;

    public CartListCommand(CartService cartService, CustomerService customerService) {
        this.cartService = cartService;
        this.customerService = customerService;
    }

    @Override
    public void run() {
        var customer = customerService.getCustomerByEmail(customerEmail);
        if (customer.isEmpty()) {
            System.out.printf("No customer found with email %s%n", customerEmail);
            return;
        }
        var optCart = cartService.getOrder(customerEmail);
        if (optCart.isPresent()) {
            var cart = optCart.get();
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
        } else {
            System.out.printf("No cart found for customer %s%n", customerEmail);
            return;
        }
    }

}
