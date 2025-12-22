package com.example.services;

import com.example.models.Category;
import com.example.repositories.CategoryRepository;

import org.jspecify.annotations.NonNull;
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

    public Category addCategory(@NonNull String name) {
        if (categoryRepository.findByNameIgnoreCase(name) != null) {
            throw new IllegalArgumentException("Category already exists");
        }
        return categoryRepository.save(new Category(name));
    }

    public boolean removeCategoryById(int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById((categoryId));
            return true;
        }
        return false;
    }
}
