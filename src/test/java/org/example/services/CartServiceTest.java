package org.example.services;

import com.example.models.*;
import com.example.respoitories.OrderItemRepository;
import com.example.respoitories.OrderRepository;
import com.example.respoitories.PaymentRepository;
import com.example.respoitories.ProductRepository;
import com.example.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private CartService cartService;

    private Customer customer;
    private Product product;
    private Orders orderEmpty;
    private OrderItem orderItem;
    private Payment payment;

    @BeforeEach
    void setup() {
        customer = new Customer("p@ntu.se", "Pontus");
        product = new Product("ThisSKU", "Billys Pizza", "Mumma!", BigDecimal.valueOf(15), true);
        orderEmpty = new Orders(customer);
        orderItem = new OrderItem(3, product.getPrice(), product, orderEmpty);
        //payment = new Payment(Payment.PaymentMethod.CARD,)
    }

    //Add to cart tests
    @Test
    void shouldAddToCartAndUpdateOrder() {
        cartService.addToCart(customer.getEmail(), product.getSku(), 2);

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
