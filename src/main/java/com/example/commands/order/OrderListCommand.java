package com.example.commands.order;

import com.example.models.Orders;
import com.example.services.OrderService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.List;

@Component
@Command(name = "list", mixinStandardHelpOptions = true, description = "List orders")
public class OrderListCommand implements Runnable {

    @Option(names = "--email", description = "Filter by customer email")
    private String email;

    @Option(names = "--status", description = "Filter by order status")
    private Orders.OrderStatus status;

    private final OrderService orderService;

    public OrderListCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void run() {
        List<Orders> orders;

        if (email != null && status != null) {
            orders = orderService.listByCustomerAndStatus(email, status);
        } else if (email != null) {
            orders = orderService.listByCustomerEmail(email);
        } else if (status != null) {
            orders = orderService.listByStatus(status);
        } else {
            orders = orderService.listAll();
        }

        if (orders.isEmpty()) {
            System.out.println("No orders found :( ");
            return;
        }

        orders.forEach(order ->
                System.out.printf(
                        "Order #%d | %-33s | %-9s | %.2f%n",
                        order.getId(),
                        order.getCustomer().getEmail(),
                        order.getStatus(),
                        order.getTotal()
                )
        );

    }
}
