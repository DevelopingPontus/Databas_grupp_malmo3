package com.example.commands;

import com.example.commands.cart.CartCommand;
import com.example.commands.product.ProductCommand;
import org.springframework.stereotype.Component;

import com.example.commands.Customer.CustomerCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "", mixinStandardHelpOptions = true, subcommands = {
        CategoryCommand.class,
        CartCommand.class,
        CustomerCommand.class,
        ProductCommand.class,
        ClearCommand.class,
        ExitCommand.class
})
public class RootCommand {

}
