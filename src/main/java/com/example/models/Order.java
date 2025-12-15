package com.example.models;

import jakarta.persistence.*;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    enum OrderStatus {
        NEW, PAID, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private double total;
    private Timestamp createdAt;

    //Relations
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

    public Order() {
    }

    public Order(Customer customer, OrderStatus status, double total, Timestamp createdAt) {
        this.customer = customer;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    public Order(Long id, Customer customer, OrderStatus status, double total, Timestamp createdAt){
        this.id = id;
        this.customer = customer;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }
}
