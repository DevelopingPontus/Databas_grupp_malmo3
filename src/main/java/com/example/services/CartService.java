package com.example.services;

import com.example.models.*;
import com.example.respoitories.*;
import jakarta.persistence.criteria.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;

    public CartService(OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       PaymentRepository paymentRepository,
                       CustomerService customerService,
                       ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addToCart(int customerId, int productId, int quantity) {
        // TODO(vera) hämta carten. (alltså order med status new) senaste ordern
        // om carten inte finns, skapa en ny
        // addera produkten till carten
        // uppdatera totalen
        // spara carten

        Customer customer = customerService.getCustomerById(customerId);
        Product product = productRepository.findById(productId);
        Order order = getCurrentOrder(customer);
        OrderItem orderItem = createOrUpdateOrderItem(order, product, quantity);

        updateOrderTotal(order);
        saveOrder(order);

    }
    // --- Hjälpmetoder till addToCart
    public Orders getCurrentOrder(Customer customer){
        return orderService.getCurrentOrder(customer)
                .orElseGet(() -> {
            Orders order = new Orders(customer);
            order.setCreatedNow();
            return orderService.save(order);
        });
    }

    public OrderItem createOrUpdateOrderItem(Order order, Product product, int quantity) {
        OrderItem item = orderItemService.findByOrderAndProduct(order, product)
                .orElseGet(() -> {
                    OrderItem oi = new OrderItem();
                    oi.setOrder(order);
                    oi.setProduct(product);
                    oi.setQuantity(0);
                    oi.setUnitPrice(product.getPrice());
                    return oi;
                });

        item.setQuantity(item.getQuantity() + quantity);
        item.setLineTotal(
                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );

        return orderItemRepository.save(item);
    }

    private void updateOrderTotal(Orders order){
        BigDecimal total = order.getOrderItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total.doubleValue());
    }

    private void saveOrder(Orders order) {
        orderService.save(order);
    }

    public void removeFromCart(int customerId, int productId, int quantity) {
        Map<Integer, Integer> cart = userCarts.get(customerId);
        if (cart != null) {
            cart.computeIfPresent(productId, (k, v) -> v > quantity ? v - quantity : null);
        }
    }

    public Map<Integer, Integer> viewCart(int customerId) {
        return new HashMap<>(userCarts.getOrDefault(customerId, Map.of()));
    }

    @Transactional
    public void checkoutTest() {
        var order = createOrder();

        try {
            reserveInventory(order);
        } catch (Exception e) {
            throw e;
        }

        var orderTotal = calculateOrderTotal(order);

        var paymentsStatus = processPayment(orderTotal);

        if (paymentsStatus == Payment.PaymentStatus.APPROVED) {
            updateOrderStatus(order, Orders.OrderStatus.PAID);
            updateInventory(order);
        } else {
            updateOrderStatus(order, Orders.OrderStatus.CANCELLED);
            throw new Exception("Payment failed");
        }
    }


    @Transactional
    public Order checkout(int customerId, Payment.PaymentMethod paymentMethod) {
        Map<Integer, Integer> cart = userCarts.get(customerId);
        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));


        //TODO: använd orderservicen istället
        // Create order
        Orders order = new Orders(customer);
        orderRepository.save(order);



        // Add order items and calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entry.getKey()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(entry.getValue());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));

            orderItemRepository.save(orderItem);
            total = total.add(orderItem.getLineTotal());
        }

        // Create payment
        Payment payment = new Payment();
        payment.setOrders(order);
        payment.setAmount(total);
        payment.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        payment.setMethod(paymentMethod);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.save(payment);

        // Update order total and status
        order.setTotal(total);
        order.setStatus(Orders.OrderStatus.PAID);
        order = orderRepository.save(order);

        // Clear the cart
        userCarts.remove(customerId);

        return order;
    }

    public void clearCart(int customerId) {
        userCarts.remove(customerId);
    }

    public BigDecimal getCartTotal(int customerId) {
        Map<Integer, Integer> cart = userCarts.get(customerId);
        if (cart == null || cart.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return cart.entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entry.getKey()));
                    return product.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}