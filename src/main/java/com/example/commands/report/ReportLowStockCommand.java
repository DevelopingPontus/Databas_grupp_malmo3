package com.example.commands.report;

import com.example.services.ReportService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;


@Component
@Command(name = "low-stock", description = "Report low stock")
public class ReportLowStockCommand implements Runnable {
    private final ReportService reportService;

    @Option(names = "threshold", description = "Threshold for low stock")
    private int threshold;

    public ReportLowStockCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void run() {
        var lowStock = reportService.getLowStockProducts(threshold);

        System.out.println("Products with low stock");
        System.out.printf("%-30s %-15s %-20s%n", "Product", "Stock", "Recomended Stock");

        for(var entry : lowStock){
            var product = entry.getKey();
            int currentStock = entry.getValue();
            int recommendedStock = currentStock * 2;

            System.out.printf("%-30s %-15d %d%n",
                    product.getName(),
                    currentStock,
                    recommendedStock);
        }
    }
}
