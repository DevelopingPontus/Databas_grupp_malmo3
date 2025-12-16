package com.example.models;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name = "payment")
public class Payment {
    //Attribut
    enum PaymentMethod {
        CARD,
        INVOICE
    }
    enum PaymentStatus {
        PENDING,
        APPROVED,
        DECLINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private PaymentMethod method;
    private PaymentStatus status;
    private Timestamp timestamp;

    //Relations
    @OneToOne
    private Order order;

    //Constructor
    public Payment() {
    }

    public Payment(PaymentMethod method, PaymentStatus status, Timestamp timestamp) {
        this.method = method;
        this.status = status;
        this.timestamp = timestamp;
    }

    //Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
