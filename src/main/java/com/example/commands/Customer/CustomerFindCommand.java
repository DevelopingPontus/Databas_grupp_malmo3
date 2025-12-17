package com.example.commands.Customer;

import org.springframework.stereotype.Component;

import com.example.services.CustomerService;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Component
@Command(name = "find", description = "Find a customer by email")
public class CustomerFindCommand implements Runnable {
    private final CustomerService customerService;

    @ArgGroup(exclusive = true, multiplicity = "1")
    private SearchCriteria search;

    static class SearchCriteria {
        @Option(names = { "-i", "--id" }, description = "The ID of the customer to find")
        Integer id;
        @Option(names = { "-e", "--email" }, description = "The email of the customer to find")
        String email;
        @Option(names = { "-n", "--name" }, description = "The name of the customer to find")
        String name;
    }

    public CustomerFindCommand(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run() {
        if (search.id != null) {
            System.out.printf("Finding customer with ID: %d\n", search.id);
            customerService.getCustomerById(search.id)
                    .ifPresentOrElse(
                            c -> System.out.printf("Found: %s, %s, %s\n", c.getName(), c.getEmail(), c.getCreatedAt()),
                            () -> System.out.println("No customer found with that ID."));
        } else if (search.email != null) {
            System.out.printf("Finding customer with Email: %s\n", search.email);
            customerService.getCustomerByEmail(search.email)
                    .ifPresentOrElse(
                            c -> System.out.printf("Found: %s, %s, %s\n", c.getName(), c.getEmail(), c.getCreatedAt()),
                            () -> System.out.println("No customer found with that email."));
        } else if (search.name != null) {
            System.out.printf("Finding customers with Name: %s\n", search.name);
            var customers = customerService.getCustomersByName(search.name);
            if (customers.isEmpty()) {
                System.out.println("No customers found with that name.");
            } else {
                customers.forEach(
                        c -> System.out.printf("Found: %s, %s, %s\n", c.getName(), c.getEmail(), c.getCreatedAt()));
            }
        }
    }
}
