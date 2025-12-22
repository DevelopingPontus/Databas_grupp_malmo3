package com.example.commands.report;

import com.example.services.ReportService;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;



@Component
@Command(name = "daily-revenue", description = "Report daily revenue")
public class ReportDailyRevenue implements Runnable{
    private final ReportService reportService;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("sv", "SE"));

    @Option(names = "days", description = "Number of days to show (default: ${DEFAULT-VALUE})",
            defaultValue = "7")
    private int days;

    public ReportDailyRevenue(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void run() {
        var dailyRevenue = reportService.getDailyRevenue(days);

        System.out.println("Daily Revenue: ");
        System.out.printf("%-15s %-15s%n", "Date", "Revenue");

        dailyRevenue.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("%-15s %s%n",
                            entry.getKey(),
                            currencyFormat.format(entry.getValue()));
                });

        double total = dailyRevenue.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

    }
}
