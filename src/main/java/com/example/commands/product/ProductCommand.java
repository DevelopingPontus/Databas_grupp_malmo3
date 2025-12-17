package com.example.commands.product;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "product", description = "Product commands", subcommands = {
        ProductAddCommand.class,
        ProductListCommand.class,
        ProductRemoveCommand.class,
        ProductUpdatedCommand.class
})
public class ProductCommand {
}
