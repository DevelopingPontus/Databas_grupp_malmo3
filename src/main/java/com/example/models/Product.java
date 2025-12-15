package com.example.models;

import jakarta.persistence.*;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

//Produkter: lista/sök (på namn, kategori), lägg till/ändra/inaktivera.
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean active;
    private Timestamp createdAt;

    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();
}
