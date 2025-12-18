package com.example.services;

import com.example.models.*;
import com.example.respoitories.*;
import jakarta.persistence.criteria.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public CartService(OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       PaymentRepository paymentRepository,
                       CustomerService customerService,
                       ProductRepository productRepository,
                       OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @Transactional
    public void addToCart(int customerId, int productId, int quantity) {

        Customer customer = customerService.getCustomerById(customerId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        Orders order = orderService.getOrCreateCart(customer);
        OrderItem orderItem = createOrUpdateOrderItem(order, product, quantity);

        updateOrderTotal(order);
        saveOrder(order);
    }

    // --- Hjälpmetoder till addToCart
    public OrderItem createOrUpdateOrderItem(Orders order, Product product, int quantity) {
        OrderItem item = orderService.findByOrderAndProduct(order, product)
                .orElseGet(() -> {
                    OrderItem oi = new OrderItem();
                    oi.setOrder(order);
                    oi.setProduct(product);
                    oi.setQuantity(0);
                    oi.setUnitPrice(product.getPrice());
                    oi.setLineTotal(BigDecimal.ZERO);
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

    @Transactional
    public void removeFromCart(int customerId, int productId, int quantity){
        if (quantity<=0){
            throw new IllegalArgumentException("Quantity most be greater than zero");
        }
        Customer customer = customerService.getCustomerById(customerId).orElseThrow(()->new IllegalArgumentException("Customer not found"))
        Orders order =  orderService.getOrCreateCart(customer);
        OrderItem item = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(" Product not in cart! "));

        int newQty = item.getQuantity() - quantity;
        if (newQty <= 0){
            order.getOrderItems().remove(item);
            orderService.delete(item);

        }


        // minska Quantity eller ta bort den
        //uppdatera orderTotal
        // spara
    }
    //public void removeFromCart(int customerId, int productId, int quantity) {
      //Map<Integer, Integer> cart = userCarts.get(customerId);
    //    if (cart != null) {
  //          cart.computeIfPresent(productId, (k, v) -> v > quantity ? v - quantity : null);
//       }
//    }
//
//    public Map<Integer, Integer> viewCart(int customerId) {
//        return new HashMap<>(userCarts.getOrDefault(customerId, Map.of()));
//    }
//
//    @Transactional
//    public void checkoutTest() {
//        var order = createOrder();
//
//        try {
//            reserveInventory(order);
//        } catch (Exception e) {
//            throw e;
//        }
//
//        var orderTotal = calculateOrderTotal(order);
//
//        var paymentsStatus = processPayment(orderTotal);
//
//        if (paymentsStatus == Payment.PaymentStatus.APPROVED) {
//            updateOrderStatus(order, Orders.OrderStatus.PAID);
//            updateInventory(order);
//        } else {
//            updateOrderStatus(order, Orders.OrderStatus.CANCELLED);
//            throw new Exception("Payment failed");
//        }
//    }
//
//
//    @Transactional
//    public Order checkout(int customerId, Payment.PaymentMethod paymentMethod) {
//        Map<Integer, Integer> cart = userCarts.get(customerId);
//        if (cart == null || cart.isEmpty()) {
//            throw new IllegalStateException("Cart is empty");
//        }
//
//        Customer customer = customerService.getCustomerById(customerId)
//                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
//
//
//        //TODO: använd orderservicen istället
//        // Create order
//        Orders order = new Orders(customer);
//        orderRepository.save(order);
//
//
//        // Add order items and calculate total
//        BigDecimal total = BigDecimal.ZERO;
//        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
//            Product product = productRepository.findById(entry.getKey())
//                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entry.getKey()));
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(product);
//            orderItem.setQuantity(entry.getValue());
//            orderItem.setUnitPrice(product.getPrice());
//            orderItem.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
//
//            orderItemRepository.save(orderItem);
//            total = total.add(orderItem.getLineTotal());
//        }
//
//        // Create payment
//        Payment payment = new Payment();
//        payment.setOrders(order);
//        payment.setAmount(total);
//        payment.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
//        payment.setMethod(paymentMethod);
//        payment.setStatus(Payment.PaymentStatus.PENDING);
//        paymentRepository.save(payment);
//
//        // Update order total and status
//        order.setTotal(total);
//        order.setStatus(Orders.OrderStatus.PAID);
//        order = orderRepository.save(order);
//
//        // Clear the cart
//        userCarts.remove(customerId);
//
//        return order;
//    }
//
//    public void clearCart(int customerId) {
//        userCarts.remove(customerId);
//    }
//
//    public BigDecimal getCartTotal(int customerId) {
//        Map<Integer, Integer> cart = userCarts.get(customerId);
//        if (cart == null || cart.isEmpty()) {
//            return BigDecimal.ZERO;
//        }
//
//        return cart.entrySet().stream()
//                .map(entry -> {
//                    Product product = productRepository.findById(entry.getKey())
//                            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + entry.getKey()));
//                    return product.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
//                })
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
}