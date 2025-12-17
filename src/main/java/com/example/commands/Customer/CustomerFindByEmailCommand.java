package com.example.commands.Customer;

import org.springframework.stereotype.Component;

import com.example.services.CustomerService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "find", description = "Find a customer by email")
public class CustomerFindByEmailCommand implements Runnable {
    private final CustomerService customerService;

    @Parameters(index = "0", description = "The email of the customer to find")
    private String email;

    public CustomerFindByEmailCommand(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run() {
        System.out.printf("Finding customer with email: %s\n", email);
        customerService.getCustomerByEmail(email).ifPresentOrElse(
                (c) -> System.out.printf("Found customer: %s, %s, %s\n", c.getName(), c.getEmail(), c.getCreatedAt()),
                () -> System.out.println("No customer found with that email."));
    }
}
