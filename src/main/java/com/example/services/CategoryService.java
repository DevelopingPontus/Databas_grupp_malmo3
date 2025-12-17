package com.example.services;

import com.example.models.Category;
import com.example.respoitories.CategoryRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(@NotNull String name) {
        categoryRepository.findByNameIgnoreCase(name).(c -> { throw new IllegalArgumentException("Category already exists"); });
        return categoryRepository.save(new Category(name));
    }


}
