package com.example.commands;

import com.example.commands.cart.CartCommand;
import com.example.commands.product.ProductCommand;
import com.example.commands.Category.CategoryCommand;
import com.example.commands.customer.CustomerCommand;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "", mixinStandardHelpOptions = true, subcommands = {
        CategoryCommand.class,
        CartCommand.class,
        CustomerCommand.class,
        ImportCommand.class,
        ProductCommand.class,
        ExitCommand.class
})
public class RootCommand {

}
