package com.example;

import java.sql.DriverManager;
import java.util.Properties;

/**
 * A health check that verifies database connectivity at application startup.
 * 
 * This provides better user feedback in case the database is unreachable,
 * preventing obscure errors later during command execution.
 */

public class DBHealthCheck {
    public static void testConnection() {
        Properties props = new Properties();
        try (var input = DBHealthCheck.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties not found in classpath");
            }
            props.load(input);
        } catch (Exception e) {
            System.err.println("Failed to load database configuration!");
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");

        try (var _ = DriverManager.getConnection(url, username, password)) {
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to connect to the database!");
            System.err.println("[ERROR] Reason: " + e.getMessage());
            System.exit(1);
        }
    }
}
