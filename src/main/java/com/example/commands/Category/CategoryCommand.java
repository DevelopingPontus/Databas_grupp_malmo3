package com.example.commands.Category;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "category", mixinStandardHelpOptions = true, description = "Commands related to category operations", subcommands = {
                CategoryAddCommand.class,
                CategoryListCommand.class
})
public class CategoryCommand {

}
