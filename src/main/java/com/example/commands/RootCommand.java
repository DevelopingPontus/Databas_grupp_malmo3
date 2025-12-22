package com.example.commands;

import com.example.commands.Category.CategoryCommand;
import com.example.commands.cart.CartCommand;
import com.example.commands.customer.CustomerCommand;
import com.example.commands.product.ProductCommand;
import com.example.commands.report.ReportCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Component
@Command(name = "", mixinStandardHelpOptions = true, subcommands = {
        CategoryCommand.class,
        CartCommand.class,
        CustomerCommand.class,
        ProductCommand.class,
        ImportCommand.class,
        ClearCommand.class,
        ExitCommand.class,
        ReportCommand.class
})
public class RootCommand {

}
