package com.example.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    //Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private Timestamp createdAt;

    //Relation
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

    //Constructor
    public Customer() {
    }

    public Customer(String email, String name, Timestamp createdAt) {
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
