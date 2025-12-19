package com.example.services;

import com.example.models.Customer;
import com.example.models.OrderItem;
import com.example.models.Orders;
import com.example.models.Product;
import com.example.respoitories.OrderItemRepository;
import com.example.respoitories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Optional<Orders> getCart(Customer customer) {
        return orderRepository.findFirstByCustomerIdAndStatusOrderByCreatedAtDesc(
                customer.getId(),
                Orders.OrderStatus.NEW);
    }

    @Transactional
    public Orders getOrCreateCart(Customer customer) {
        return orderRepository
                .findFirstByCustomerIdAndStatusOrderByCreatedAtDesc(
                        customer.getId(),
                        Orders.OrderStatus.NEW)
                .orElseGet(() -> {
                    Orders order = new Orders(customer);
                    order.setCreatedNow();
                    return orderRepository.save(order);
                });
    }

    @Transactional
    public void updateTotal(Orders order) {
        double total = order.getOrderItems().stream()
                .mapToDouble(orderItem -> orderItem.getLineTotal().doubleValue())
                .sum();
        order.setTotal(total);
    }

    public Orders save(Orders order) {
        return orderRepository.save(order);
    }

    public Optional<OrderItem> findByOrderAndProduct(Orders order, Product product) {
        return orderItemRepository.findByOrderAndProduct(order, product);
    }

    public void deleteOrderItem(OrderItem item) {
        Orders order = item.getOrder();

        if (order != null) {
            order.getOrderItems().remove(item);
        }
        orderItemRepository.delete(item);

    }

    // Hämta pågående order (NEW) för kund
    // Skapa ny order om ingen finns
    // Uppdatera total
    // Spara order

    // orderItemService -NEJ
}
