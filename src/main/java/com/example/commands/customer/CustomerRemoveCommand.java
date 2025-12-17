package com.example.commands.customer;

import org.springframework.stereotype.Component;

import com.example.services.CustomerService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "remove", description = "Remove a customer by ID")
public class CustomerRemoveCommand implements Runnable {
    private final CustomerService customerService;

    @Parameters(index = "0", description = "The ID of the customer to remove")
    private int customerId;

    public CustomerRemoveCommand(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run() {
        System.out.printf("Removing customer with ID: %d\n", customerId);
        boolean removed = customerService.removeCustomerById(customerId);
        if (removed) {
            System.out.println("Customer removed successfully.");
        } else {
            System.out.println("No customer found with that ID.");
        }
    }
}
