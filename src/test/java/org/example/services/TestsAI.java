//package org.example.services;
//
//import com.example.models.*;
//import com.example.respoitories.OrderItemRepository;
//import com.example.respoitories.OrderRepository;
//import com.example.respoitories.PaymentRepository;
//import com.example.services.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TestsAI {
//
//    private static final String TEST_EMAIL = "test@example.com";
//    private static final String TEST_SKU = "TEST123";
//    private static final int TEST_QUANTITY = 2;
//
//    @Mock
//    private OrderRepository orderRepository;
//    @Mock
//    private OrderItemRepository orderItemRepository;
//    @Mock
//    private PaymentRepository paymentRepository;
//    @Mock
//    private CustomerService customerService;
//    @Mock
//    private ProductService productService;
//    @Mock
//    private OrderService orderService;
//    @Mock
//    private InventoryService inventoryService;
//    @Mock
//    private PaymentService paymentService;
//
//    @InjectMocks
//    private CartService cartService;
//
//    private Customer testCustomer;
//    private Product testProduct;
//    private Orders testOrder;
//    private OrderItem testOrderItem;
//    private Payment testPayment;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize test data
//        testCustomer = new Customer();
//        testCustomer.setId(1);
//        testCustomer.setEmail(TEST_EMAIL);
//        testCustomer.setName("Test User");
//
//        testProduct = new Product();
//        testProduct.setId(1);
//        testProduct.setSku(TEST_SKU);
//        testProduct.setName("Test Product");
//        testProduct.setPrice(BigDecimal.TEN);
//        testProduct.setActive(true);
//
//        testOrder = new Orders();
//        //testOrder.setId(1L);
//        testOrder.setCustomer(testCustomer);
//        testOrder.setStatus(Orders.OrderStatus.NEW);
//
//        testOrderItem = new OrderItem();
//        //testOrderItem.setId(1L);
//        testOrderItem.setProduct(testProduct);
//        testOrderItem.setQuantity(TEST_QUANTITY);
//        testOrderItem.setUnitPrice(testProduct.getPrice());
//        testOrderItem.setLineTotal(testProduct.getPrice().multiply(BigDecimal.valueOf(TEST_QUANTITY)));
//
//        testPayment = new Payment();
//        //testPayment.setId(1L);
//        testPayment.setMethod(Payment.PaymentMethod.CARD);
//        testPayment.setStatus(Payment.PaymentStatus.APPROVED);
//    }
//
//    @Test
//    void addToCart_ShouldAddNewItemToCart() {
//        // Arrange
//        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
//        when(productService.searchProductBySku(TEST_SKU)).thenReturn(java.util.List.of(testProduct));
//        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
//        when(inventoryService.getStock(testProduct.getId())).thenReturn(10);
//        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        cartService.addToCart(TEST_EMAIL, TEST_SKU, TEST_QUANTITY);
//
//        // Assert
//        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
//        verify(orderService, times(1)).save(testOrder);
//    }
//
//    @Test
//    void addToCart_ShouldThrowWhenNotEnoughStock() {
//        // Arrange
//        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
//        //  when(productService.searchProductBySku(TEST_SKU)).thenReturn(java.util.List.of(testProduct));
//        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
//        when(inventoryService.getStock(testProduct.getId())).thenReturn(1);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> cartService.addToCart(TEST_EMAIL, TEST_SKU, 2));
//    }
//
//    @Test
//    void removeFromCart_ShouldRemoveItem() {
//        // Arrange
//        testOrder.getOrderItems().add(testOrderItem);
//        // when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
//        // when(productService.searchProductBySku(TEST_SKU)).thenReturn(java.util.List.of(testProduct));
//        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
//
//        // Act
//        cartService.removeFromCart(TEST_EMAIL, TEST_SKU);
//
//        // Assert
//        assertTrue(testOrder.getOrderItems().isEmpty());
//        verify(orderService, times(1)).save(testOrder);
//    }
//
//    @Test
//    void checkout_ShouldProcessPaymentAndUpdateOrder() {
//        // Arrange
//        testOrder.getOrderItems().add(testOrderItem);
//        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
//        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
//        when(paymentService.process(any(Orders.class), any(Payment.PaymentMethod.class)))
//                .thenReturn(testPayment);
//
//        // Act
//        cartService.checkout(TEST_EMAIL, Payment.PaymentMethod.CARD);
//
//        // Assert
//        assertEquals(Orders.OrderStatus.PAID, testOrder.getStatus());
//        verify(inventoryService, times(1)).reserve(testOrder);
//        verify(paymentService, times(1)).process(any(), any());
//        verify(orderService, times(1)).save(testOrder);
//    }
//
//    @Test
//    void checkout_ShouldThrowWhenCartIsEmpty() {
//        // Arrange
//        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
//        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> cartService.checkout(TEST_EMAIL, Payment.PaymentMethod.CARD));
//    }
//
//    @Test
//    void getOrder_ShouldReturnCart() {
//        // Arrange
//        testOrder.getOrderItems().add(testOrderItem);
//        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
//        when(orderService.getCart(testCustomer)).thenReturn(Optional.of(testOrder));
//
//        // Act
//        Optional<Orders> result = cartService.getOrder(TEST_EMAIL);
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals(1, result.get().getOrderItems().size());
//    }
//}
