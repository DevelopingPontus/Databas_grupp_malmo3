package com.example.commands.order;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "order", mixinStandardHelpOptions = true, description = "Commands related to orders", subcommands = {
        OrderListCommand.class,
        OrderShowCommand.class
})
public class OrderCommand {
}
