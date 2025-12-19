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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    private final int productQuantityInInventory;
    private final int moreThenQuantityInInventory;
    private final int lessThenQuantityInInventory;
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
    private Inventory inventory;
    private Orders orderEmpty;
    private OrderItem orderItem;
    private Payment payment;

    {
        productQuantityInInventory = 5;
        moreThenQuantityInInventory = 10;
        lessThenQuantityInInventory = 1;
    }

    @BeforeEach
    void setup() {
        customer = new Customer("p@ntu.se", "Pontus");
        product = new Product("ThisSKU", "Billys Pizza", "Mumma!", BigDecimal.valueOf(15), true);
        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(productQuantityInInventory);
        orderEmpty = new Orders(customer);
        orderItem = new OrderItem(lessThenQuantityInInventory, product.getPrice(), product, orderEmpty);
        //payment = new Payment(Payment.PaymentMethod.CARD,)
    }

    //Add to cart tests
    @Test
    void shouldAddToCartAndUpdateOrder() {
        assertEquals(orderService.getCart(customer), Optional.empty());

        cartService.addToCart(customer.getEmail(), product.getSku(), lessThenQuantityInInventory);

        assertEquals(orderService.getCart(customer), customer);
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
