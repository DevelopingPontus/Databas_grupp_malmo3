package com.example.services;

import com.example.models.*;
import com.example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Import({CartService.class, CustomerService.class, ProductService.class,
        OrderService.class, InventoryService.class, PaymentService.class})
class CartServiceJPATest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private Customer testCustomer;
    private Product testProduct;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_SKU = "TEST123";
    private final int TEST_QUANTITY = 2;

    @BeforeEach
    void setUp() {
        // Clear existing data
        orderItemRepository.deleteAll();
        paymentRepository.deleteAll();
        orderRepository.deleteAll();
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        // Create test data
        testCustomer = new Customer("Test User", TEST_EMAIL);
        testCustomer = customerRepository.save(testCustomer);

        testProduct = new Product(TEST_SKU, "Test Product", "Test Description", BigDecimal.TEN);
        testProduct = productRepository.save(testProduct);

        Inventory testInventory = new Inventory(testProduct, 10); // 10 items in stock
        inventoryRepository.save(testInventory);
    }

    @Test
    void shouldAddItemToCart() {
        // When
        cartService.addToCart(TEST_EMAIL, TEST_SKU, TEST_QUANTITY);
        System.out.println(1);
        // Then
        Orders cart = orderRepository.findFirstByCustomerIdAndStatusOrderByCreatedAtDesc(
                testCustomer.getId(),
                Orders.OrderStatus.NEW
        ).orElseThrow();

        assertEquals(1, cart.getOrderItems().size());
        OrderItem item = cart.getOrderItems().iterator().next();
        assertEquals(TEST_QUANTITY, item.getQuantity());
        assertEquals(testProduct, item.getProduct());
        assertEquals(testProduct.getPrice().multiply(BigDecimal.valueOf(TEST_QUANTITY)), item.getLineTotal());
    }

    // ... rest of the test methods
}