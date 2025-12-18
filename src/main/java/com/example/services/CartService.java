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
    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    public CartService(OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       PaymentRepository paymentRepository,
                       CustomerService customerService,
                       ProductRepository productRepository,
                       OrderService orderService,
                       InventoryService inventoryService,
                       PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
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
        // Creating OrderItem id manually
        // because OrderItem has a composite primary key
        OrderItem.OrderItemId id = new OrderItem.OrderItemId(order.getId(), product.getId());

        OrderItem item = orderItemRepository.findById(id).orElseGet(OrderItem::new);

        item.setOrderAndProduct(order, product);
        item.setQuantity(item.getQuantity() + quantity);
        item.setUnitPrice(product.getPrice());
        item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

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
    public void removeFromCart(int customerId, int productId, int quantity) {

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Orders order = orderService.getOrCreateCart(customer);

        OrderItem.OrderItemId id = new OrderItem.OrderItemId(customerId, productId);

        orderItemRepository.findById(id).ifPresent(item -> {
            int newQty = item.getQuantity() - quantity;

            if (newQty <= 0) {
                order.getOrderItems().remove(item);
                orderItemRepository.delete(item);
            } else {
                item.setQuantity(newQty);
                item.setLineTotal(
                        item.getUnitPrice().multiply(BigDecimal.valueOf(newQty))
                );
            }
        });
        orderService.updateTotal(order);
        orderService.save(order);
    }

    // get cart here, is looped and printed in list command!
    @Transactional(readOnly = true)
    public Orders getCartItems(int customerId){

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Orders order = orderService.getOrCreateCart(customer);

        order.getOrderItems().size();
        return order;
    }

    // TODO:clearCart(customerId)

    // checkout(customerId, PaymentMethod method)
    // Checkout =
//    ta kundens cart (Order NEW) →
//    reservera lager →
//    beräkna total →
//    simulera betalning →
//    uppdatera orderstatus →
//    spara →
//    töm cart
    @Transactional
    public void checkout(int customerId, Payment.PaymentMethod paymentMethod){

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Orders order = orderService.getOrCreateCart(customer);

        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // reserve in inventory
        inventoryService.reserve(order);

        // update total
        orderService.updateTotal(order);

        // create and process payment
        Payment payment = paymentService.process(order, paymentMethod);

        if (payment.getStatus() == Payment.PaymentStatus.APPROVED) {
            order.setStatus(Orders.OrderStatus.PAID);
        } else {
            order.setStatus(Orders.OrderStatus.CANCELLED);
            inventoryService.release(order);
            throw new IllegalArgumentException("Payment failed");

        }

        orderService.save(order);
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
//        var order = createOrder(); (orderService)
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