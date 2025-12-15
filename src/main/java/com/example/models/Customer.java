package com.example.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private Timestamp createdAt;

    //Relation
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

    public Customer() {
    }

    public Customer(String email, String name, Timestamp createdAt) {
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }

    public Customer(long id, String email, String name, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }
}
