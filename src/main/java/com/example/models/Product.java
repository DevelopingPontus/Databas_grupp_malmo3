package com.example.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

//Produkter: lista/sök (på namn, kategori), lägg till/ändra/inaktivera.
@Entity
@Table(name = "product")
public class Product {
    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Integer id;
    @Column
    private String sku;
    @Column
    private String name;
    @Column
    private String description;
    @Column(columnDefinition = "numeric(10,2) default 0.0")
    private double price;
    @Column
    private boolean active;
    @Column
    private Timestamp createdAt;

    //Relations
    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @OneToOne(mappedBy = "product")
    private OrderItem orderItem;

    @OneToOne(mappedBy = "product")
    private Inventory inventory;

    //Constructor
    public Product() {
    }

    public Product(String sku, String name, String description, double price, boolean active, Timestamp createdAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.createdAt = createdAt;
    }

    //Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

//    public Set<OrderItem> getOrderItems() {
//        return orderItems;
//    }
//
//    public void setOrderItems(Set<OrderItem> orderItems) {
//        this.orderItems = orderItems;
//    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
