package com.example.commands.Category;

import com.example.services.CategoryService;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "add", mixinStandardHelpOptions = true, description = "Add new category")
public class CategoryAddCommand implements Runnable {
    private final CategoryService categoryService;

    @CommandLine.Parameters(index = "0", description = "The name of the category")
    private String name;

    public CategoryAddCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run() {
        var category = categoryService.addCategory(name);
        System.out.println("Added category: " + category.getName());
    }
}
