package com.example.commands.Category;


import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "category", description = "Commands related to category operations", subcommands = {
        CategoryAddCommand.class,
        CategoryListCommand.class
})
public class CategoryCommand {

}

