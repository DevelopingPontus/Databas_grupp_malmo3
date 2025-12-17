package com.example.models;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    //Attributs
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVENTORY_ID")
    private Integer id;
    @Column(insertable = false, updatable = false)
    private Integer product_Id;
    @Column
    private int quantity; //KRAV(MVP) säger att vi ska ha endast inStock dock vår sql schema har quantity men inte inStock .

    //Relations
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    //Constructor
    public Inventory() {
    }

    public Inventory(int quantity) {
        this.quantity = quantity;
    }

    //Getters and Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_Id() {
        return product_Id;
    }

    public void setProduct_Id(Integer product_Id) {
        this.product_Id = product_Id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
