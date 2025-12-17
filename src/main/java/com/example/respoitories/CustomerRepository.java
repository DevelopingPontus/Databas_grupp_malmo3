package com.example.respoitories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Optional<Customer> findByEmail(String email);
}
