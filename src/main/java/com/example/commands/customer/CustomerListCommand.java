package com.example.commands.customer;

import org.springframework.stereotype.Component;

import com.example.services.CustomerService;

import picocli.CommandLine.Command;

import java.util.stream.Collectors;

@Component
@Command(name = "list", mixinStandardHelpOptions = true, description = "List all customers")
public class CustomerListCommand implements Runnable {
    private final CustomerService customerService;

    public CustomerListCommand(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run() {
        System.out.println("Listing all customers...");
        customerService.getAllCustomers().forEach((c) -> System.out.printf(
                "ID:%-4d | Name:%-18s | Email:%-33s | Created at:%.2s%n",
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getCreatedAt()));
    }
}
