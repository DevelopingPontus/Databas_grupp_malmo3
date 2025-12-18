package com.example.commands.cart;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "cart", description = "Commands related to cart operations", subcommands = {
        CartAddCommand.class
})
public class CartCommand {

}
