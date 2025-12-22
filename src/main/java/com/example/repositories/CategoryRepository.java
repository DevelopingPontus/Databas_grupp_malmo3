package com.example.repositories;

import com.example.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Object findByNameIgnoreCase(String name);

    @Query("SELECT c.name FROM Category c")
    List<String> findAllCategoryNames();
}
