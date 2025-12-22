package com.example.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.models.Customer;

@DataJpaTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findByEmail_existingEmail_returnsCustomer() {
        Customer customer = new Customer("Alice", "alice@example.com");
        customerRepository.save(customer);
        var result = customerRepository.findByEmail("alice@example.com");
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    public void findByEmail_nonExistingEmail_returnsEmpty() {
        var result = customerRepository.findByEmail("non.existing@example.com");
        assertTrue(result.isEmpty());
    }

    @Test
    public void findById_existingId_returnsCustomer() {
        Customer customer = new Customer("Bob", "bob@example.com");
        customerRepository.save(customer);
        var result = customerRepository.findById(customer.getId());
        assertTrue(result.isPresent());
        assertEquals("Bob", result.get().getName());
    }

    @Test
    public void findById_nonExistingId_returnsEmpty() {
        var result = customerRepository.findById(999);
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByName_existingName_returnsCustomers() {
        Customer customer1 = new Customer("Charlie", "charlie@example.com");
        Customer customer2 = new Customer("Charlie", "charlie2@example.com");
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        var result = customerRepository.findByName("Charlie");
        assertEquals(2, result.size());
    }

    @Test
    public void findByName_nonExistingName_returnsEmptyList() {
        var result = customerRepository.findByName("NonExistingName");
        assertTrue(result.isEmpty());
    }
}
