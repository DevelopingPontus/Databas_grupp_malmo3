package com.example.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

//Produkter: lista/sök (på namn, kategori), lägg till/ändra/inaktivera.
@Entity
@Table(name = "product")
public class Product {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Integer id;
    @Column(nullable = false, unique = true)
    private String sku;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String description = "Description missing";
    @Column(columnDefinition = "numeric(10,2) default 0.0", nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private boolean active;
    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    // Relations
    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private final Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory inventory;

    // Constructor
    private Product() {
    }

    public Product(String sku, String name, String description, double price) {
        this(sku, name, description, price, true);
    }

    public Product(String sku, String name, String description, BigDecimal price) {
        this(sku, name, description, price, true);
    }

    public Product(String sku, String name, String description, double price, boolean active) {
        this(sku, name, description, new BigDecimal(price), active);
    }

    public Product(String sku, String name, String description, BigDecimal price, boolean active) {
        this.sku = sku;
        this.name = name;
        if (description != null)
            this.description = description;
        this.price = price;
        this.active = active;
    }

    // Getters and Setters
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getProducts().add(this);
    }

    // public Set<OrderItem> getOrderItems() {
    // return orderItems;
    // }
    //
    // public void setOrderItems(Set<OrderItem> orderItems) {
    // this.orderItems = orderItems;
    // }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
//
//    @PrePersist
//    @PreUpdate
//    private void normalizeSku() {
//        this.sku = this.sku.toLowerCase();
//    }
}
