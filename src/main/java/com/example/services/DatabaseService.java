package com.example.services;

import org.springframework.stereotype.Service;

import com.example.respoitories.CategoryRepository;
import com.example.respoitories.CustomerRepository;
import com.example.respoitories.InventoryRepository;
import com.example.respoitories.OrderRepository;
import com.example.respoitories.PaymentRepository;
import com.example.respoitories.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class DatabaseService {
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    public DatabaseService(CategoryRepository categoryRepository, CustomerRepository customerRepository,
            InventoryRepository inventoryRepository, OrderRepository orderRepository,
            PaymentRepository paymentRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void clearAll() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    public void clearCategories() {
        categoryRepository.deleteAll();
    }

    public void clearCustomers() {
        customerRepository.deleteAll();
    }

    public void clearInventory() {
        inventoryRepository.deleteAll();
    }

    public void clearOrders() {
        orderRepository.deleteAll();
    }

    public void clearPayments() {
        paymentRepository.deleteAll();
    }

    public void clearProducts() {
        productRepository.deleteAll();
    }
}
