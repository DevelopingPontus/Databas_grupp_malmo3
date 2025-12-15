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

    public Product() {
    }

    public Product(String name, String description, double price, boolean active, Timestamp createdAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Product(long id, String name, String description, double price, boolean active, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.createdAt = createdAt;
    }
}
