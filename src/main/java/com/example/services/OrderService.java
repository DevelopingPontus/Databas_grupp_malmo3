package com.example.services;

import com.example.models.Customer;
import com.example.models.OrderItem;
import com.example.models.Orders;
import com.example.models.Product;
import com.example.respoitories.OrderItemRepository;
import com.example.respoitories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Transactional
    public Optional<Orders> findById(int id) {
        Orders order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.getOrderItems().size(); // init lazy
        }
        return Optional.ofNullable(order);
    }

    //    order list // visa all
    @Transactional
    public List<Orders> listAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    //    order list --email john.doe@example.com // lista kundens
    @Transactional
    public List<Orders> listByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    //    order list --status NEW // filtrera för status
    @Transactional
    public List<Orders> listByStatus(Orders.OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    //      order list efter customer (mail) och status
    @Transactional
    public List<Orders> listByCustomerAndStatus(String email, Orders.OrderStatus status) {
        return orderRepository.findByCustomerEmailAndStatusOrderByCreatedAtDesc(email, status);
    }





}
