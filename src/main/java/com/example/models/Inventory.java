package com.example.models;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    //Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity; //KRAV(MVP) säger att vi ska ha endast inStock dock vår sql schema har quantity men inte inStock .
    private boolean inStock;

    //Relations
    @OneToOne
    private Product product;

    //Constructor
    public Inventory() {
    }

    public Inventory(int quantity, boolean inStock) {
        this.quantity = quantity;
        this.inStock = inStock;
    }

    //Getters and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
