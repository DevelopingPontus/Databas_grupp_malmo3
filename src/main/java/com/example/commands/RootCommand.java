package com.example.commands;

import com.example.commands.product.ProductCommand;
import org.springframework.stereotype.Component;

import com.example.commands.Customer.CustomerCommand;

import picocli.CommandLine.Command;

@Component
@Command(name = "", mixinStandardHelpOptions = true, subcommands = {
        CustomerCommand.class,
        ProductCommand.class,
        ExitCommand.class
})
public class RootCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Welcome to the E-Store application! Type a command or 'exit' to quit.");
    }
}
