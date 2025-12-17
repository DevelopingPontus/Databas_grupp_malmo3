package com.example.commands.Customer;

import org.springframework.stereotype.Component;

import com.example.respoitories.CustomerRepository;

import picocli.CommandLine.Command;

@Component
@Command(name = "list", description = "List all customers")
public class CustomerListCommand implements Runnable {
    private final CustomerRepository customerRepository;

    public CustomerListCommand(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run() {
        System.out.println("Listing all customers...");
        customerRepository.findAll().forEach((c) -> System.out.println(c.getName()));
    }

}
