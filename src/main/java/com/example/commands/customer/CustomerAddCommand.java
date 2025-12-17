package com.example.commands.customer;

import org.springframework.stereotype.Component;

import com.example.services.CustomerService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "add", description = "Add a new customer")
public class CustomerAddCommand implements Runnable {
    private final CustomerService customerService;

    @Parameters(index = "0", description = "The name of the customer")
    private String name;

    @Parameters(index = "1", description = "The email of the customer")
    private String email;

    public CustomerAddCommand(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run() {
        var customer = customerService.addCustomer(name, email);
        System.out.println("Added customer: " + customer.getName() + " with email: " + customer.getEmail());
    }

}
