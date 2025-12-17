package com.example.commands.Customer;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Component
@Command(name = "customer", description = "Commands related to customer operations", subcommands = {
                CustomerListCommand.class,
                CustomerAddCommand.class,
                CustomerFindByEmailCommand.class,
                CustomerRemoveCommand.class
})
public class CustomerCommand {

}
