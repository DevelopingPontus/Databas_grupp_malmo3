package com.example.services;

import com.example.models.*;
import com.example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_SKU = "TEST123";
    private final int TEST_QUANTITY = 2;

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    @Mock
    private InventoryRepository inventoryRepository;
    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentService paymentService;

    @Spy
    @InjectMocks
    private CartService cartService;

    private Customer testCustomer;
    private Product testProduct;
    private Orders testOrder;
    private OrderItem testOrderItem;
    private Payment testPayment;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testCustomer = new Customer("Test User", TEST_EMAIL);

        testProduct = new Product(TEST_SKU, "Test Product", "Nom", BigDecimal.TEN);

        testOrder = new Orders(testCustomer);

        testInventory = new Inventory(testProduct, TEST_QUANTITY);
    }


    //Add to cart tests
    @Test
    void shouldGetCustomerByEmail() {
        //Arrange
        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));

        //Act and Assert
        assertEquals(customerRepository.findByEmail(TEST_EMAIL), Optional.of(testCustomer));
    }

    @Test
    void shouldThrowWhenGivenWrongEmail() {
        //Arrange
        when(customerService.getCustomerByEmail("wrong@mail.who")).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(NoSuchElementException.class, () -> customerRepository.findByEmail("wrong@mail.who").orElseThrow());
    }

    //Create or update order item tests
//    @Test
//    void shouldAddToCart() {
//        //Arrange
//        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
//        System.out.println(1);
//        when(productService.searchProductBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
//        System.out.println(2);
//        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
//        System.out.println(3);
//        when(inventoryRepository.findByProductId(testProduct.getId())).thenReturn(Optional.of(testInventory));
//        System.out.println(4);
//        // when(inventoryService.getStock(testProduct.getId())).thenReturn(TEST_QUANTITY + 1);
//        System.out.println(5);
//
//
//        doReturn(testOrderItem)
//                .when(cartService)
//                .createOrUpdateOrderItem(testOrder, testProduct, TEST_QUANTITY);
//
//        //Act
//        cartService.addToCart(TEST_EMAIL, TEST_SKU, TEST_QUANTITY);
//
//
//        //verify(orderService).save(testOrder);
//    }

}
