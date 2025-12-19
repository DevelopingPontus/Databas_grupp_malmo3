package com.example.commands.inventory;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(name = "inventory", mixinStandardHelpOptions = true, description = "Commands related to inventory", subcommands = {
        InventoryListCommand.class
})
public class InventoryCommand {


}
