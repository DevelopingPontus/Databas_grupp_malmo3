package com.example.commands;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Component
@Command(name = "exit", description = "Exit the application")
public class ExitCommand implements Runnable {

    public static boolean exitRequested = false;

    @Override
    public void run() {
        exitRequested = true;
    }
}
