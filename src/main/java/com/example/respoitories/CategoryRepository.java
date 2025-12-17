package com.example.respoitories;

import com.example.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Integer> {
    Object findByNameIgnoreCase(String name);
    boolean exists(String name);
}
