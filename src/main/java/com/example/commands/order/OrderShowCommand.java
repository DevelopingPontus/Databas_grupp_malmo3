package com.example.commands.order;

import ch.qos.logback.core.testUtil.RunnableWithCounterAndDone;
import com.example.services.OrderService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Component
@Command(name = "show", mixinStandardHelpOptions = true, description = "Show order details")
public class OrderShowCommand implements Runnable {

    @Parameters(index = "0", description = "Order id")
    private int orderId;

    private final OrderService orderService;

    public OrderShowCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void run() {
        orderService.findById(orderId).ifPresentOrElse(order -> {

            System.out.printf(
                    "Order  #%d  |  %s  |  %s  |  %.2f%n",
                    order.getId(),
                    order.getCustomer().getEmail(),
                    order.getStatus(),
                    order.getTotal()
            );

            if (order.getOrderItems().isEmpty()) {
                System.out.println("No items found");
                return;
            }

            order.getOrderItems().forEach(item ->
                    System.out.printf(
                            "– %s  x%d  = %s%n",
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getLineTotal()
                    )
            );

        }, () -> System.out.println("Order not found"));

    }
}
