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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Import({CartService.class, CustomerService.class, ProductService.class,
        OrderService.class, InventoryService.class, PaymentService.class})
class CartServiceJPATest {

    @Autowired
    private JsonService jsonService;

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

    @Autowired
    private CategoryRepository categoryRepository;

    private Orders order;
    private OrderItem orderItem;
    private Payment payment;
    private Customer testCustomer;
    private Product testProduct;
    private String TEST_EMAIL;
    private String TEST_SKU;
    private int TEST_QUANTITY;

    @BeforeEach
    void setUp() {
        loadTestData();
        testCustomer = customerRepository.findById(1).get();
        TEST_EMAIL = testCustomer.getEmail();
        testProduct = productRepository.findProductBySku("ELEC001").get();
        TEST_SKU = testProduct.getSku();
        TEST_QUANTITY = 2;
    }

//    void setUp() {
//        // Clear existing data
//        orderItemRepository.deleteAll();
//        paymentRepository.deleteAll();
//        orderRepository.deleteAll();
//        inventoryRepository.deleteAll();
//        productRepository.deleteAll();
//        customerRepository.deleteAll();
//
//        // Create test data
//        testCustomer = new Customer("Test User", TEST_EMAIL);
//        testCustomer = customerRepository.save(testCustomer);
//
//        testProduct = new Product(TEST_SKU, "Test Product", "Test Description", BigDecimal.TEN);
//        testProduct = productRepository.save(testProduct);
//
//        Inventory testInventory = new Inventory(testProduct, 10); // 10 items in stock
//        inventoryRepository.save(testInventory);
//    }

    private void loadTestData() {
        try {
            String jsonData = new String(Files.readAllBytes(
                    Paths.get("test_data_small.json"))); // or use the full path
            jsonService.importData(jsonData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data", e);
        }
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