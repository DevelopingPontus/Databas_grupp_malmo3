package com.example.services;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import com.example.models.Customer;
import com.example.respoitories.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByEmail(@NonNull String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer addCustomer(@NonNull String name, @NonNull String email) {
        Customer customer = new Customer(name, email);
        return customerRepository.save(customer);
    }

    public boolean removeCustomerById(int customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }
}
