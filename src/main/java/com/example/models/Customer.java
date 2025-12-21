package com.example.models;

import com.example.exceptions.InvalidEmailException;
import com.example.exceptions.InvalidNameException;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Integer id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    // Relation
    @OneToMany(mappedBy = "customer")
    private Set<Orders> orders = new HashSet<>();

    // Needed for JPA
    public Customer() {
        // Empty, just for JPA
    }

    public Customer(String name, String email) {
        this(email, name, null);
    }

    public Customer(String email, String name, Timestamp createdAt) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email cannot be null or blank");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidEmailException("Invalid email format");
        }
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    public void addOrder(Orders order) {
        orders.add(order);
        order.setCustomer(this);
    }
}
