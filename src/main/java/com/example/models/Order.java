package com.example.models;

import jakarta.persistence.*;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    //Attribut
    enum OrderStatus {
        NEW, PAID, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private double total;
    private Timestamp createdAt;

    //Relations
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne
    private Payment payment;

    @ManyToOne
    private Customer customer;

    //Constructor
    public Order() {
    }

    public Order(OrderStatus status, double total, Timestamp createdAt) {
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    //Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
