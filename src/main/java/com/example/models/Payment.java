package com.example.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "payment_method")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PaymentMethod method;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "payment_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PaymentStatus status;
    @Column
    private Timestamp timestamp;
    // Relations
    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    /**
     * Don't use this constructor
     */
    private Payment() {
    }

    public Payment(Orders order, PaymentMethod method) {
        this(order, method, PaymentStatus.PENDING, Timestamp.valueOf(LocalDateTime.now()));
    }

    // Constructor

    public Payment(Orders order, PaymentMethod method, PaymentStatus status) {
        this(order, method, status, Timestamp.valueOf(LocalDateTime.now()));
    }

    public Payment(PaymentMethod method, PaymentStatus status, Timestamp timestamp) {
        this(null, method, status, timestamp);
    }

    public Payment(Orders order, PaymentMethod method, PaymentStatus status, Timestamp timestamp) {
        this.orders = order;
        this.method = method;
        this.status = status;
        this.timestamp = timestamp;
        if (this.orders != null) {
            this.orders.setPayment(this);
        }
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    // Attribut
    public enum PaymentMethod {
        CARD,
        INVOICE
    }

    public enum PaymentStatus {
        PENDING,
        APPROVED,
        DECLINED
    }
}
