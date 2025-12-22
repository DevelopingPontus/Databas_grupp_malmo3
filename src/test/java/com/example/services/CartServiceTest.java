package com.example.services;

import com.example.models.*;
import com.example.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_SKU = "TEST123";
    private static final int TEST_QUANTITY = 2;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private CartService cartService;

    private Customer testCustomer;
    private Product testProduct;
    private Orders testOrder;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Create test customer
        testCustomer = new Customer("Test User", TEST_EMAIL);
        testCustomer.setId(1);

        // Create test category
        testCategory = new Category("Electronics");
        testCategory.setId(1);

        // Create test product
        testProduct = new Product(TEST_SKU, "Test Product", "A test product",
                new BigDecimal("99.99"));
        testProduct.setId(1);

        // Create test order (cart)
        testOrder = new Orders(testCustomer);
        testOrder.setId(1);
        testOrder.setStatus(Orders.OrderStatus.NEW);
    }

    @Test
    void addToCart_newProduct_shouldAddItemToCart() {
        // Arrange
        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(productService.searchProductBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
        when(inventoryService.getStock(testProduct.getId())).thenReturn(10);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        cartService.addToCart(TEST_EMAIL, TEST_SKU, TEST_QUANTITY);

        // Assert
        verify(customerService).getCustomerByEmail(TEST_EMAIL);
        verify(productService).searchProductBySku(TEST_SKU);
        verify(orderService).getOrCreateCart(testCustomer);
        verify(inventoryService).getStock(testProduct.getId());
        verify(orderItemRepository).save(any(OrderItem.class));
        verify(orderService).save(testOrder);
        assertEquals(1, testOrder.getOrderItems().size());
    }

    @Test
    void addToCart_existingProduct_shouldUpdateQuantity() {
        // Arrange
        int initialQuantity = 2;
        int additionalQuantity = 3;

        // Create existing order item
        OrderItem existingItem = new OrderItem(initialQuantity, testProduct.getPrice(), testProduct, testOrder);
        existingItem.setLineTotal(testProduct.getPrice().multiply(BigDecimal.valueOf(initialQuantity)));
        testOrder.addOrderItem(existingItem);

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(productService.searchProductBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
        when(inventoryService.getStock(testProduct.getId())).thenReturn(10);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        cartService.addToCart(TEST_EMAIL, TEST_SKU, additionalQuantity);

        // Assert
        verify(orderItemRepository).save(any(OrderItem.class));
        verify(orderService).save(testOrder);
        assertEquals(1, testOrder.getOrderItems().size());
        // Verify the quantity was updated correctly
        OrderItem updatedItem = testOrder.getOrderItems().iterator().next();
        assertEquals(initialQuantity + additionalQuantity, updatedItem.getQuantity());
    }

    @Test
    void addToCart_insufficientStock_shouldThrowException() {
        // Arrange
        int requestedQuantity = 100;

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(productService.searchProductBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
        when(inventoryService.getStock(testProduct.getId())).thenReturn(5); // Only 5 in stock

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addToCart(TEST_EMAIL, TEST_SKU, requestedQuantity);
        });

        assertTrue(exception.getMessage().contains("Not enough stock"));
        verify(orderItemRepository, never()).save(any(OrderItem.class));
        verify(orderService, never()).save(any(Orders.class));
    }

    @Test
    void addToCart_customerNotFound_shouldThrowException() {
        // Arrange
        String nonexistentEmail = "nonexistent@example.com";

        when(customerService.getCustomerByEmail(nonexistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> {
            cartService.addToCart(nonexistentEmail, TEST_SKU, TEST_QUANTITY);
        });

        verify(productService, never()).searchProductBySku(anyString());
        verify(orderService, never()).getOrCreateCart(any(Customer.class));
    }

    @Test
    void addToCart_productNotFound_shouldThrowException() {
        // Arrange
        String nonexistentSku = "NONEXISTENT";

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(productService.searchProductBySku(nonexistentSku)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> {
            cartService.addToCart(TEST_EMAIL, nonexistentSku, TEST_QUANTITY);
        });

        verify(orderService, never()).getOrCreateCart(any(Customer.class));
    }

    @Test
    void checkout_validCart_shouldCompleteCheckout() {
        // Arrange
        Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.CARD;

        // Add items to cart
        OrderItem item1 = new OrderItem(2, testProduct.getPrice(), testProduct, testOrder);
        item1.setLineTotal(testProduct.getPrice().multiply(BigDecimal.valueOf(2)));
        testOrder.addOrderItem(item1);
        testOrder.setTotal(item1.getLineTotal());

        Payment successfulPayment = new Payment(testOrder, paymentMethod, Payment.PaymentStatus.APPROVED);

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
        when(paymentService.process(testOrder, paymentMethod)).thenReturn(successfulPayment);

        // Act
        cartService.checkout(TEST_EMAIL, paymentMethod);

        // Assert
        verify(inventoryService).reserve(testOrder);
        verify(orderService).updateTotal(testOrder);
        verify(paymentService).process(testOrder, paymentMethod);
        verify(orderService).save(testOrder);
        assertEquals(Orders.OrderStatus.PAID, testOrder.getStatus());
    }

    @Test
    void checkout_emptyCart_shouldThrowException() {
        // Arrange
        Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.CARD;

        // Empty cart
        Orders emptyOrder = new Orders(testCustomer);
        emptyOrder.setId(1);

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(emptyOrder);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.checkout(TEST_EMAIL, paymentMethod);
        });

        assertTrue(exception.getMessage().contains("Cart is empty"));
        verify(inventoryService, never()).reserve(any(Orders.class));
        verify(paymentService, never()).process(any(Orders.class), any(Payment.PaymentMethod.class));
    }

    @Test
    void checkout_paymentFailed_shouldCancelOrderAndReleaseInventory() {
        // Arrange
        Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.CARD;

        // Add items to cart
        OrderItem item1 = new OrderItem(2, testProduct.getPrice(), testProduct, testOrder);
        item1.setLineTotal(testProduct.getPrice().multiply(BigDecimal.valueOf(2)));
        testOrder.addOrderItem(item1);
        testOrder.setTotal(item1.getLineTotal());

        Payment failedPayment = new Payment(testOrder, paymentMethod, Payment.PaymentStatus.DECLINED);

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
        when(paymentService.process(testOrder, paymentMethod)).thenReturn(failedPayment);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.checkout(TEST_EMAIL, paymentMethod);
        });

        assertTrue(exception.getMessage().contains("Payment failed"));
        verify(inventoryService).reserve(testOrder);
        verify(inventoryService).release(testOrder);
        assertEquals(Orders.OrderStatus.CANCELLED, testOrder.getStatus());
    }

    @Test
    void checkout_multipleItems_shouldCalculateCorrectTotal() {
        // Arrange
        Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.INVOICE;

        // Create second product
        Product product2 = new Product("TEST002", "Test Product 2", "Another test",
                new BigDecimal("49.99"));
        product2.setId(2);

        // Add multiple items to cart
        OrderItem item1 = new OrderItem(2, testProduct.getPrice(), testProduct, testOrder);
        item1.setLineTotal(testProduct.getPrice().multiply(BigDecimal.valueOf(2)));

        OrderItem item2 = new OrderItem(3, product2.getPrice(), product2, testOrder);
        item2.setLineTotal(product2.getPrice().multiply(BigDecimal.valueOf(3)));

        testOrder.addOrderItem(item1);
        testOrder.addOrderItem(item2);

        BigDecimal expectedTotal = item1.getLineTotal().add(item2.getLineTotal());
        testOrder.setTotal(expectedTotal);

        Payment successfulPayment = new Payment(testOrder, paymentMethod, Payment.PaymentStatus.APPROVED);

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
        when(paymentService.process(testOrder, paymentMethod)).thenReturn(successfulPayment);

        // Act
        cartService.checkout(TEST_EMAIL, paymentMethod);

        // Assert
        verify(orderService).updateTotal(testOrder);
        verify(paymentService).process(testOrder, paymentMethod);
        assertEquals(Orders.OrderStatus.PAID, testOrder.getStatus());
        assertEquals(2, testOrder.getOrderItems().size());
    }

    @Test
    void checkout_customerNotFound_shouldThrowException() {
        // Arrange
        String nonexistentEmail = "nonexistent@example.com";
        Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.CARD;

        when(customerService.getCustomerByEmail(nonexistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.checkout(nonexistentEmail, paymentMethod);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
        verify(orderService, never()).getOrCreateCart(any(Customer.class));
        verify(paymentService, never()).process(any(Orders.class), any(Payment.PaymentMethod.class));
    }

    @Test
    void createOrUpdateOrderItem_newItem_shouldCreateNewOrderItem() {
        // Arrange
        int quantity = 3;

        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderItem result = cartService.createOrUpdateOrderItem(testOrder, testProduct, quantity);

        // Assert
        assertNotNull(result);
        assertEquals(quantity, result.getQuantity());
        assertEquals(testProduct.getPrice(), result.getUnitPrice());
        assertEquals(testProduct.getPrice().multiply(BigDecimal.valueOf(quantity)), result.getLineTotal());
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void createOrUpdateOrderItem_existingItem_shouldUpdateQuantity() {
        // Arrange
        int initialQuantity = 2;
        int additionalQuantity = 3;

        OrderItem existingItem = new OrderItem(initialQuantity, testProduct.getPrice(), testProduct, testOrder);
        existingItem.setLineTotal(testProduct.getPrice().multiply(BigDecimal.valueOf(initialQuantity)));
        testOrder.addOrderItem(existingItem);

        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderItem result = cartService.createOrUpdateOrderItem(testOrder, testProduct, additionalQuantity);

        // Assert
        assertNotNull(result);
        assertEquals(initialQuantity + additionalQuantity, result.getQuantity());
        assertEquals(testProduct.getPrice().multiply(BigDecimal.valueOf(5)), result.getLineTotal());
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void removeFromCart_existingItem_shouldRemoveItem() {
        // Arrange
        OrderItem item = new OrderItem(2, testProduct.getPrice(), testProduct, testOrder);
        testOrder.addOrderItem(item);

        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(productService.searchProductBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);

        // Act
        cartService.removeFromCart(TEST_EMAIL, TEST_SKU);

        // Assert
        assertTrue(testOrder.getOrderItems().isEmpty());
        verify(orderService).updateTotal(testOrder);
        verify(orderService).save(testOrder);
    }

    @Test
    void removeFromCart_customerNotFound_shouldThrowException() {
        // Arrange
        String nonexistentEmail = "nonexistent@example.com";

        when(customerService.getCustomerByEmail(nonexistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.removeFromCart(nonexistentEmail, TEST_SKU);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
        verify(orderService, never()).save(any(Orders.class));
    }

    @Test
    void getOrder_existingCart_shouldReturnCart() {
        // Arrange
        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(orderService.getCart(testCustomer)).thenReturn(Optional.of(testOrder));

        // Act
        Optional<Orders> result = cartService.getOrder(TEST_EMAIL);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testOrder, result.get());
        verify(customerService).getCustomerByEmail(TEST_EMAIL);
        verify(orderService).getCart(testCustomer);
    }

    @Test
    void getOrder_noCart_shouldReturnEmpty() {
        // Arrange
        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        when(orderService.getCart(testCustomer)).thenReturn(Optional.empty());

        // Act
        Optional<Orders> result = cartService.getOrder(TEST_EMAIL);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getOrder_customerNotFound_shouldThrowException() {
        // Arrange
        String nonexistentEmail = "nonexistent@example.com";

        when(customerService.getCustomerByEmail(nonexistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getOrder(nonexistentEmail);
        });

        assertTrue(exception.getMessage().contains("Customer not found"));
    }
}
