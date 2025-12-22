package com.example.services;

import com.example.models.Customer;
import com.example.models.Product;
import com.example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JsonService.class)
class CartServiceJPATest {

    @Autowired
    private TestEntityManager entityManager;

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

    @Autowired
    private JsonService jsonService;

    @Autowired
    private CartService cartService;

    private Customer testCustomer;
    private Product testProduct;
    private String TEST_EMAIL;
    private String TEST_SKU;
    private int TEST_QUANTITY;

    @BeforeEach
    void setUp() {
        loadTestData();
        testCustomer = customerRepository.findByEmail("john.doe@example.com")
                .orElseThrow(() -> new RuntimeException("Test customer not found"));
        TEST_EMAIL = testCustomer.getEmail();
        testProduct = productRepository.findProductBySku("ELEC001")
                .orElseThrow(() -> new RuntimeException("Test product not found"));
        TEST_SKU = testProduct.getSku();
        TEST_QUANTITY = 2;
    }

    private void loadTestData() {
        try {
            String jsonData = new String(getClass().getClassLoader()
                    .getResourceAsStream("test_data_small.json").readAllBytes());

            // Clear existing data first
            paymentRepository.deleteAll();
            orderItemRepository.deleteAll();
            orderRepository.deleteAll();
            customerRepository.deleteAll();
            inventoryRepository.deleteAll();
            productRepository.deleteAll();
            categoryRepository.deleteAll();

            // Import test data
            jsonService.importData(jsonData);
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data: " + e.getMessage(), e);
        }
    }
//
//    @Test
//    void shouldAddItemToCart() {
//        // When
//        cartService.addToCart(TEST_EMAIL, TEST_SKU, TEST_QUANTITY);
//
//        // Then
//        Orders cart = orderRepository.findFirstByCustomerIdAndStatusOrderByCreatedAtDesc(
//                testCustomer.getId(),
//                Orders.OrderStatus.NEW
//        ).orElseThrow(() -> new RuntimeException("No cart found for customer"));
//
//        assertEquals(1, cart.getOrderItems().size(), "Cart should contain exactly one item");
//        OrderItem item = cart.getOrderItems().iterator().next();
//        assertEquals(TEST_QUANTITY, item.getQuantity(), "Item quantity should match");
//        assertEquals(TEST_SKU, item.getProduct().getSku(), "Product SKU should match");
//        item = cart.getOrderItems().iterator().next();
//        assertEquals(TEST_QUANTITY, item.getQuantity());
//        assertEquals(testProduct, item.getProduct());
//        assertEquals(testProduct.getPrice().multiply(BigDecimal.valueOf(TEST_QUANTITY)), item.getLineTotal());
//    }
}