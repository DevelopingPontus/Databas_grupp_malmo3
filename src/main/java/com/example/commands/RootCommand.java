package com.example.commands;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Component
@Command(name = "", helpCommand = true, subcommands = {
        ExitCommand.class
})
public class RootCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Welcome to the E-Store application! Type a command or 'exit' to quit.");
    }
}
