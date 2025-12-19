package org.example.services;

import com.example.models.*;
import com.example.respoitories.*;
import com.example.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_SKU = "TEST123";
    private final int TEST_QUANTITY = 2;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private CustomerService customerService;
    @Mock
    private DatabaseService databaseService;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private OrderService orderService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private CartService cartService = new CartService(orderRepository, orderItemRepository, paymentRepository, customerService, productService, orderService, inventoryService, paymentService);

    private Customer testCustomer;
    private Product testProduct;
    private Orders testOrder;
    private OrderItem testOrderItem;
    private Payment testPayment;

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


    }


    //Add to cart tests
    @Test
    void shouldAddToCartAndUpdateOrder() {

    }

    @Test
    void shouldThrowWhenAddingMoreThenThereAreInStock() {

    }

    //Create or update order item tests
    @Test
    void shouldMakeNewOrderItemWithParametersAndSave() {

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
