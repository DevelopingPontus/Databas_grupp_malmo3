package com.example.commands;

import org.springframework.stereotype.Component;

import com.example.commands.customer.CustomerCommand;

import picocli.CommandLine.Command;

@Component
@Command(name = "", mixinStandardHelpOptions = true, subcommands = {
        CustomerCommand.class,
        ExitCommand.class
})
public class RootCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Welcome to the E-Store application! Type a command or 'exit' to quit.");
    }
}
