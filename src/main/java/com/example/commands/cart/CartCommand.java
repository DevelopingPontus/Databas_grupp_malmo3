package com.example.commands.cart;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "cart", mixinStandardHelpOptions = true, description = "Commands related to cart operations", subcommands = {
                CartAddCommand.class,
                CartRemoveItemCommand.class,
                CartListCommand.class,
                CartCheckoutCommand.class
})
public class CartCommand {

}
