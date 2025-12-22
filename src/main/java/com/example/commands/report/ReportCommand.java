package com.example.commands.report;

import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "report", description = "Report commands", subcommands = {
                ReportTopSellersCommand.class,
                ReportLowStockCommand.class,
                ReportDailyRevenue.class
})
public class ReportCommand {

}
