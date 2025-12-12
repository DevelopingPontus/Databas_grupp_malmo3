package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Test database connection at startup
        DBHealthCheck.testConnection();

        // Start the Spring Boot application
        SpringApplication.run(Main.class, args);
    }
}