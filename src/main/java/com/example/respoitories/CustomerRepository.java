package com.example.respoitories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
