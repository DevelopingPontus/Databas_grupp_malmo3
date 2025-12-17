package com.example.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "orders")
public class Orders {
    // Attribut
    enum OrderStatus {
        NEW, PAID, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Integer id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(columnDefinition = "numeric(10,2) default 0.0")
    private double total;
    @Column()
    private Timestamp createdAt;

    // Relations
    // @OneToMany(mappedBy = "order")
    // private Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne(mappedBy = "orders")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Constructor
    public Orders() {
    }

    public Orders(OrderStatus status, double total, Timestamp createdAt) {
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    // public Set<OrderItem> getOrderItems() {
    // return orderItems;
    // }
    //
    // public void setOrderItems(Set<OrderItem> orderItems) {
    // this.orderItems = orderItems;
    // }

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
