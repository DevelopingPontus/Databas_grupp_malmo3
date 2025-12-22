package com.example.services;

import com.example.models.*;
import com.example.repositories.*;
import com.example.util.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

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

    @Autowired
    private CategoryRepository categoryRepository;

    private Customer testCustomer;
    private Product testProduct;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_SKU = "TEST123";
    private final int TEST_QUANTITY = 2;

    @BeforeEach
    void setUp() throws IOException {
        // Load test data
        List<Category> categories = TestDataLoader.loadTestData(
                "test-data/test_data_small.json",
                "categories",
                Category.class
        );

        List<Product> products = TestDataLoader.loadTestData(
                "test-data/test_data_small.json",
                "products",
                Product.class
        );

        // Save test data to database
        categoryRepository.saveAll(categories);
        productRepository.saveAll(products);
        // Add more data as needed
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