package com.example.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.example.models.Category;
import com.example.models.Customer;
import com.example.models.Inventory;
import com.example.models.OrderItem;
import com.example.models.Orders;
import com.example.models.Payment;
import com.example.models.Product;
import com.example.respoitories.CategoryRepository;
import com.example.respoitories.CustomerRepository;
import com.example.respoitories.InventoryRepository;
import com.example.respoitories.OrderItemRepository;
import com.example.respoitories.OrdersRepository;
import com.example.respoitories.PaymentRepository;
import com.example.respoitories.ProductRepository;

@Service
public class JsonService {
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    public JsonService(CategoryRepository categoryRepository, CustomerRepository customerRepository,
            InventoryRepository inventoryRepository, OrderItemRepository orderItemRepository,
            OrdersRepository ordersRepository, PaymentRepository paymentRepository,
            ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderItemRepository = orderItemRepository;
        this.ordersRepository = ordersRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
    }

    public void importData(String json) {
        JSONObject jsonObject = new JSONObject(json);

        JSONArray categories = jsonObject.getJSONArray("categories");
        JSONArray products = jsonObject.getJSONArray("products");
        JSONArray customers = jsonObject.getJSONArray("customers");
        JSONArray inventory = jsonObject.getJSONArray("inventory");
        JSONArray orders = jsonObject.getJSONArray("orders");

        var importedCategories = parseCategories(categories);
        categoryRepository.saveAll(importedCategories);
        var importedProducts = parseProducts(products, importedCategories);
        productRepository.saveAll(importedProducts.values());
        var importedCustomers = parseCustomers(customers);
        customerRepository.saveAll(importedCustomers);
        var importedInventory = parseInventory(inventory, importedProducts);
        inventoryRepository.saveAll(importedInventory);
        var importedOrders = parseOrders(orders, importedCustomers, importedProducts);
        int orderIndex = 0;
        for (Orders order : importedOrders) {
            orderIndex++;

            // Initialize composite keys for order items BEFORE saving order
            for (OrderItem item : order.getOrderItems()) {
                item.setId(new OrderItem.OrderItemId());
            }

            var payment = order.getPayment();
            order.setPayment(null);

            // This will cascade save to OrderItems automatically
            ordersRepository.save(order);
            System.out.println("Saved order " + orderIndex + " with ID: " + order.getId() +
                    " and " + order.getOrderItems().size() + " items");

            if (payment != null) {
                paymentRepository.save(payment);
                order.setPayment(payment);
            }
        }
    }

    private List<Category> parseCategories(JSONArray categoriesArray) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < categoriesArray.length(); i++) {
            JSONObject categoryJson = categoriesArray.getJSONObject(i);
            String name = categoryJson.getString("name");
            Category category = new Category(name);
            categories.add(category);
        }
        return categories;
    }

    private HashMap<String, Product> parseProducts(JSONArray productsArray, List<Category> categories) {
        HashMap<String, Product> products = new HashMap<>();
        for (int i = 0; i < productsArray.length(); i++) {
            JSONObject productJson = productsArray.getJSONObject(i);
            String sku = productJson.getString("sku");
            String name = productJson.getString("name");
            String description = productJson.optString("description", null);
            double price = productJson.getDouble("price");
            boolean active = productJson.getBoolean("active");
            Product product = new Product(sku, name, description, price, active);
            JSONArray categoryNames = productJson.getJSONArray("categoryNames");
            for (int j = 0; j < categoryNames.length(); j++) {
                String categoryName = categoryNames.getString(j);
                categories.stream()
                        .filter(c -> c.getName().equals(categoryName))
                        .findFirst()
                        .ifPresent(product::addCategory);
            }
            products.put(sku, product);
        }
        return products;
    }

    private List<Customer> parseCustomers(JSONArray customersArray) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < customersArray.length(); i++) {
            JSONObject customerJson = customersArray.getJSONObject(i);
            String name = customerJson.getString("name");
            String email = customerJson.getString("email");
            Timestamp createdAt = customerJson.optString("createdAt", null) != null
                    ? Timestamp.valueOf(customerJson.getString("createdAt"))
                    : new Timestamp(System.currentTimeMillis());
            Customer customer = new Customer(email, name, createdAt);
            customers.add(customer);
        }
        return customers;
    }

    private List<Inventory> parseInventory(JSONArray inventoryArray, HashMap<String, Product> products) {
        List<Inventory> inventoryList = new ArrayList<>();
        for (int i = 0; i < inventoryArray.length(); i++) {
            JSONObject inventoryJson = inventoryArray.getJSONObject(i);
            Product product = products.get(inventoryJson.getString("productSku"));
            int quantity = inventoryJson.getInt("quantity");
            Inventory inventory = new Inventory(product, quantity);
            product.setInventory(inventory);
            inventoryList.add(inventory);
        }
        return inventoryList;
    }

    private List<Orders> parseOrders(JSONArray ordersArray, List<Customer> customers,
            HashMap<String, Product> products) {
        List<Orders> ordersList = new ArrayList<>();
        for (int i = 0; i < ordersArray.length(); i++) {
            JSONObject orderJson = ordersArray.getJSONObject(i);
            String customerEmail = orderJson.getString("customerEmail");
            Customer customer = customers.stream()
                    .filter(c -> c.getEmail().equals(customerEmail))
                    .findFirst()
                    .orElse(null);
            if (customer == null) {
                System.out.printf("Customer with email %s not found. Skipping order.\n", customerEmail);
                continue;
            }
            Orders.OrderStatus status = Orders.OrderStatus.valueOf(orderJson.getString("status").toUpperCase());
            Timestamp createdAt = orderJson.optString("createdAt", null) != null
                    ? Timestamp.valueOf(orderJson.getString("createdAt"))
                    : new Timestamp(System.currentTimeMillis());
            Orders order = new Orders(status, 0.0, createdAt);
            order.setCustomer(customer);
            var itemsArray = orderJson.getJSONArray("items");
            double total = 0.0;
            for (int j = 0; j < itemsArray.length(); j++) {
                JSONObject itemJson = itemsArray.getJSONObject(j);
                Product product = products.get(itemJson.getString("productSku"));
                int quantity = itemJson.getInt("quantity");
                double price = itemJson.getDouble("unitPrice");
                total += price * quantity;
                OrderItem orderItem = new OrderItem(quantity, BigDecimal.valueOf(price), product, order);
                order.addOrderItem(orderItem);
            }
            if (status == Orders.OrderStatus.PAID) {
                JSONObject paymentJson = orderJson.getJSONObject("payment");
                Payment.PaymentMethod method = Payment.PaymentMethod
                        .valueOf(paymentJson.getString("method").toUpperCase());
                Payment.PaymentStatus paymentStatus = Payment.PaymentStatus
                        .valueOf(paymentJson.getString("status").toUpperCase());
                Timestamp paymentTimestamp = paymentJson.optString("timestamp", null) != null
                        ? Timestamp.valueOf(paymentJson.getString("timestamp"))
                        : new Timestamp(System.currentTimeMillis());
                Payment payment = new Payment(method, paymentStatus, paymentTimestamp);
                payment.setOrders(order);
                order.setPayment(payment);
            }
            order.setTotal(total);
            customer.addOrder(order);
            ordersList.add(order);
        }
        return ordersList;
    }
}
