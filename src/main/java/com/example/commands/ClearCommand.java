package com.example.commands;

import org.springframework.stereotype.Component;

import com.example.services.DatabaseService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "clear", mixinStandardHelpOptions = true, description = "Clear the database")
public class ClearCommand implements Runnable {
    @Option(names = "--all", description = "Clear all data")
    private boolean all;
    @Option(names = "--categories", description = "Clear categories")
    private boolean categories;
    @Option(names = "--customers", description = "Clear customers")
    private boolean customers;
    @Option(names = "--inventory", description = "Clear inventory")
    private boolean inventory;
    @Option(names = "--orders", description = "Clear orders")
    private boolean orders;
    @Option(names = "--payments", description = "Clear payments")
    private boolean payments;
    @Option(names = "--products", description = "Clear products")
    private boolean products;

    private final DatabaseService databaseService;

    public ClearCommand(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void run() {
        if (all) {
            databaseService.clearAll();
            System.out.println("All data cleared.");
        } else {
            if (categories) {
                databaseService.clearCategories();
                System.out.println("Categories cleared.");
            }
            if (customers) {
                databaseService.clearCustomers();
                System.out.println("Customers cleared.");
            }
            if (inventory) {
                databaseService.clearInventory();
                System.out.println("Inventory cleared.");
            }
            if (orders) {
                databaseService.clearOrders();
                System.out.println("Orders cleared.");
            }
            if (payments) {
                databaseService.clearPayments();
                System.out.println("Payments cleared.");
            }
            if (products) {
                databaseService.clearProducts();
                System.out.println("Products cleared.");
            }
        }
    }

}
