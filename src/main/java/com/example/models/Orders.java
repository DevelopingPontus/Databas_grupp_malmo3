package com.example.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Orders {
    // Relations
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<OrderItem> orderItems = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "order_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private OrderStatus status;
    @Column(columnDefinition = "numeric(10,2) default 0.0")
    private BigDecimal total;
    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Constructor
    private Orders() {
    }

    public Orders(Customer customer) {
        this.customer = customer;
        this.total = BigDecimal.valueOf(0);
        this.status = OrderStatus.NEW;
        customer.addOrder(this);
    }

    public Orders(OrderStatus status, double total, Timestamp createdAt) {
        this(status, BigDecimal.valueOf(total), createdAt);
    }

    public Orders(OrderStatus status, BigDecimal total, Timestamp createdAt) {
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    public void setOrderDate(LocalDateTime now) {
    }

    public void setCreatedNow() {
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = BigDecimal.valueOf(total);
    }

    public void setTotal(BigDecimal total) {
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

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
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

    // Attribut
    public enum OrderStatus {
        NEW, PAID, CANCELLED
    }
}
