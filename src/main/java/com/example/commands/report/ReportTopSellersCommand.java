package com.example.commands.report;


import com.example.services.ReportService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "top-sellers", description = "Report top sellers")
public class ReportTopSellersCommand implements Runnable {
    private final ReportService reportService;

    @Option(names = {"limit"}, description = "Number of products to show (default: ${DEFAULT-VALUE})",
            defaultValue = "10")
    private int limit;

    public ReportTopSellersCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void run() {
        var topSellers = reportService.getTopSellingProducts(limit);

        System.out.println("Best selling products: ");
        System.out.printf("%-30s %-15s %-15s%n", "Product", "Sold", "Revenue");

        for (var entry : topSellers.entrySet()) {
            var product = entry.getKey();
            int quantity = entry.getValue();

            if (product != null) {
                try {
                    double revenue = product.getPrice().doubleValue() * quantity;
                    System.out.printf("%-30s %-15d %,.2f kr%n",
                            product.getName(),
                            quantity,
                            revenue);
                } catch (Exception e) {
                    System.err.println("Error calculating revenue for product " +
                            (product.getName() != null ? product.getName() : "unknown product"));
                }
            }
        }
    }
}
