package com.example.commands.customer;

import org.springframework.stereotype.Component;

import com.example.services.CustomerService;

import picocli.CommandLine.Command;

@Component
@Command(name = "list", description = "List all customers")
public class CustomerListCommand implements Runnable {
    private final CustomerService customerService;

    public CustomerListCommand(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run() {
        System.out.println("Listing all customers...");
        customerService.getAllCustomers()
                .forEach((c) -> System.out.printf("%s, %s, %s\n", c.getName(), c.getEmail(), c.getCreatedAt()));
    }

}
