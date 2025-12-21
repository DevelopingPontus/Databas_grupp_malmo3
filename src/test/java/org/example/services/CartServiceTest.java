package org.example.services;

import com.example.models.*;
import com.example.respoitories.*;
import com.example.services.*;
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
import static org.mockito.Mockito.doReturn;
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
        testCustomer = new Customer();
        testCustomer.setEmail(TEST_EMAIL);
        testCustomer.setName("Test User");

        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setSku(TEST_SKU);
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.TEN);
        testProduct.setActive(true);

        testOrder = new Orders();
        testOrder.setCustomer(testCustomer);
        testOrder.setStatus(Orders.OrderStatus.NEW);

        testOrderItem = new OrderItem();
        testOrderItem.setProduct(testProduct);
        testOrderItem.setQuantity(TEST_QUANTITY);
        testOrderItem.setUnitPrice(testProduct.getPrice());
        testOrderItem.setLineTotal(testProduct.getPrice().multiply(BigDecimal.valueOf(TEST_QUANTITY)));

        testPayment = new Payment();
        testPayment.setId(1);
        testPayment.setMethod(Payment.PaymentMethod.CARD);
        testPayment.setStatus(Payment.PaymentStatus.PENDING);

        testInventory = new Inventory();
        testInventory.setId(1);
        testInventory.setProduct(testProduct);
        testInventory.setProduct_Id(testProduct.getId());
        testInventory.setQuantity(TEST_QUANTITY + 1);
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
    @Test
    void shouldAddToCart() {
        //Arrange
        when(customerService.getCustomerByEmail(TEST_EMAIL)).thenReturn(Optional.of(testCustomer));
        System.out.println(1);
        when(productService.searchProductBySku(TEST_SKU)).thenReturn(Optional.of(testProduct));
        System.out.println(2);
        when(orderService.getOrCreateCart(testCustomer)).thenReturn(testOrder);
        System.out.println(3);
        when(inventoryRepository.findByProductId(testProduct.getId())).thenReturn(Optional.of(testInventory));
        System.out.println(4);
        // when(inventoryService.getStock(testProduct.getId())).thenReturn(TEST_QUANTITY + 1);
        System.out.println(5);


        doReturn(testOrderItem)
                .when(cartService)
                .createOrUpdateOrderItem(testOrder, testProduct, TEST_QUANTITY);

        //Act
        cartService.addToCart(TEST_EMAIL, TEST_SKU, TEST_QUANTITY);


        //verify(orderService).save(testOrder);
    }

    @Test
    void shouldNotMakeNewOrderItem() {

    }

    @Test
    void shouldFindCurrentOrderItemUpdateItAndSaveIt() {

    }

    //Update order total tests
    @Test
    void shouldUpdateOrderTotal() {

    }

    //Remove from cart tests
    @Test
    void shouldUpdateOrderQuantity() {

    }

    @Test
    void shouldThrowWhenRemovingMoreQuantityOfItemThenThereIsInCart() {

    }

    //Get cart Items tests
    @Test
    void shouldReturnEverythingInACart() {

    }
}
