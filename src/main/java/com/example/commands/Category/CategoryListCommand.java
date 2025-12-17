package com.example.commands.Category;

import com.example.services.CategoryService;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "list", description = "List all categories")
public class CategoryListCommand implements Runnable {
    private final CategoryService categoryService;

    public CategoryListCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run() {
        System.out.println("Listing all categories...");
        categoryService.listAllCategories()
                .forEach((c) -> System.out.printf("%s %s \n", c.getId(), c.getName()));
    }
}
