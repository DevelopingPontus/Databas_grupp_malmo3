package com.example.services;

import com.example.models.*;
import com.example.repositories.OrderItemRepository;
import com.example.repositories.OrderRepository;
import com.example.repositories.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    public CartService(OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            PaymentRepository paymentRepository,
            CustomerService customerService,
            ProductService productService,
            OrderService orderService,
            InventoryService inventoryService,
            PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository = paymentRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.orderService = orderService;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
    }

    @Transactional
    public void addToCart(String customerEmail, String productSku, int quantity) {

        Customer customer = customerService.getCustomerByEmail(customerEmail).orElseThrow();
        Product product = productService.searchProductBySku(productSku).orElseThrow(); // Was .get now .orElseThrow
        Orders order = orderService.getOrCreateCart(customer);

        if (inventoryService.getStock(product.getId()) < quantity) {
            throw new IllegalArgumentException("Not enough stock for product " + productSku);
        }
        OrderItem orderItem = createOrUpdateOrderItem(order, product, quantity);

        saveOrder(order);
        updateOrderTotal(order);
    }

    // --- Hjälpmetoder till addToCart
    public OrderItem createOrUpdateOrderItem(Orders order, Product product, int quantity) {
        var orderItem = order.getOrderItems().stream().filter((oi) -> oi.getProduct().getId().equals(product.getId()))
                .findFirst();
        if (orderItem.isPresent()) {
            OrderItem item = orderItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            return orderItemRepository.save(item);
        } else {
            OrderItem item = new OrderItem(quantity, product.getPrice(), product, order);
            order.addOrderItem(item);
            item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            return orderItemRepository.save(item);
        }
    }

    private void updateOrderTotal(Orders order) {
        BigDecimal total = order.getOrderItems().stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
    }

    private void saveOrder(Orders order) {
        orderService.save(order);
    }

    @Transactional
    public void removeFromCart(String customerEmail, String productSku) {

        Customer customer = customerService.getCustomerByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Product product = productService.searchProductBySku(productSku).get();
        Orders order = orderService.getOrCreateCart(customer);

        var orderItems = order.getOrderItems();
        orderItems.removeIf((item) -> item.getProduct().getId().equals(product.getId()));

        orderService.updateTotal(order);
        orderService.save(order);
    }

    // get cart here, is looped and printed in list command!
    @Transactional(readOnly = true)
    public Optional<Orders> getOrder(String customerEmail) {

        Customer customer = customerService.getCustomerByEmail(customerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        var order = orderService.getCart(customer);
        // Force loading of order items
        if (order.isPresent()) {
            var cart = order.get();
            cart.getOrderItems().size();
        }

        return order;
    }

    // TODO:clearCart(customerId)

    // checkout(customerId, PaymentMethod method)
    // Checkout =
    // ta kundens cart (Order NEW) →
    // reservera lager →
    // beräkna total →
    // simulera betalning →
    // uppdatera orderstatus →
    // spara →
    // töm cart
    @Transactional
    public void checkout(String customerEmail, Payment.PaymentMethod paymentMethod) {

        Customer customer = customerService.getCustomerByEmail(customerEmail)
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
            // NOTE: This is inside transaction, so if exception is thrown, all changes will
            // be rolled back
            order.setStatus(Orders.OrderStatus.CANCELLED);
            inventoryService.release(order);
            throw new IllegalArgumentException("Payment failed");

        }

        orderService.save(order);
    }

    // public void removeFromCart(int customerId, int productId, int quantity) {
    // Map<Integer, Integer> cart = userCarts.get(customerId);
    // if (cart != null) {
    // cart.computeIfPresent(productId, (k, v) -> v > quantity ? v - quantity :
    // null);
    // }
    // }
    //
    // public Map<Integer, Integer> viewCart(int customerId) {
    // return new HashMap<>(userCarts.getOrDefault(customerId, Map.of()));
    // }
    //
    // @Transactional
    // public void checkoutTest() {
    // var order = createOrder(); (orderService)
    //
    // try {
    // reserveInventory(order);
    // } catch (Exception e) {
    // throw e;
    // }
    //
    // var orderTotal = calculateOrderTotal(order);
    //
    // var paymentsStatus = processPayment(orderTotal);
    //
    // if (paymentsStatus == Payment.PaymentStatus.APPROVED) {
    // updateOrderStatus(order, Orders.OrderStatus.PAID);
    // updateInventory(order);
    // } else {
    // updateOrderStatus(order, Orders.OrderStatus.CANCELLED);
    // throw new Exception("Payment failed");
    // }
    // }
    //
    //
    // @Transactional
    // public Order checkout(int customerId, Payment.PaymentMethod paymentMethod) {
    // Map<Integer, Integer> cart = userCarts.get(customerId);
    // if (cart == null || cart.isEmpty()) {
    // throw new IllegalStateException("Cart is empty");
    // }
    //
    // Customer customer = customerService.getCustomerById(customerId)
    // .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    //
    //
    // //TODO: använd orderservicen istället
    // // Create order
    // Orders order = new Orders(customer);
    // orderRepository.save(order);
    //
    //
    // // Add order items and calculate total
    // BigDecimal total = BigDecimal.ZERO;
    // for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
    // Product product = productRepository.findById(entry.getKey())
    // .orElseThrow(() -> new IllegalArgumentException("Product not found: " +
    // entry.getKey()));
    //
    // OrderItem orderItem = new OrderItem();
    // orderItem.setOrder(order);
    // orderItem.setProduct(product);
    // orderItem.setQuantity(entry.getValue());
    // orderItem.setUnitPrice(product.getPrice());
    // orderItem.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
    //
    // orderItemRepository.save(orderItem);
    // total = total.add(orderItem.getLineTotal());
    // }
    //
    // // Create payment
    // Payment payment = new Payment();
    // payment.setOrders(order);
    // payment.setAmount(total);
    // payment.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
    // payment.setMethod(paymentMethod);
    // payment.setStatus(Payment.PaymentStatus.PENDING);
    // paymentRepository.save(payment);
    //
    // // Update order total and status
    // order.setTotal(total);
    // order.setStatus(Orders.OrderStatus.PAID);
    // order = orderRepository.save(order);
    //
    // // Clear the cart
    // userCarts.remove(customerId);
    //
    // return order;
    // }
    //
    // public void clearCart(int customerId) {
    // userCarts.remove(customerId);
    // }
    //
    // public BigDecimal getCartTotal(int customerId) {
    // Map<Integer, Integer> cart = userCarts.get(customerId);
    // if (cart == null || cart.isEmpty()) {
    // return BigDecimal.ZERO;
    // }
    //
    // return cart.entrySet().stream()
    // .map(entry -> {
    // Product product = productRepository.findById(entry.getKey())
    // .orElseThrow(() -> new IllegalArgumentException("Product not found: " +
    // entry.getKey()));
    // return product.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
    // })
    // .reduce(BigDecimal.ZERO, BigDecimal::add);
    // }
}