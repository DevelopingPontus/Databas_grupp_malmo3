package com.example.commands.customer;

import org.springframework.stereotype.Component;

import com.example.exceptions.InvalidEmailException;
import com.example.exceptions.InvalidNameException;
import com.example.services.CustomerService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "add", mixinStandardHelpOptions = true, description = "Add a new customer")
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
        try {
            var customer = customerService.addCustomer(name, email);
            System.out.println("Added customer: " + customer.getName() + " with email: " + customer.getEmail());
        } catch (InvalidNameException e) {
            System.err.println(e.getMessage());
        } catch (InvalidEmailException e) {
            System.err.println(e.getMessage());
        }
    }

}
