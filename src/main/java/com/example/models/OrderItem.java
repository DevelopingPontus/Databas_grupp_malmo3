package com.example.models;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id = new OrderItemId();

    //Relations
    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private double unitPrice;
    private double lineTotal;

    public OrderItem() {
    }

    public OrderItem(Order order, Product product, int quantity, double unitPrice, double lineTotal) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public OrderItem(OrderItemId OrderItemId, Order order, Product product, int quantity, double unitPrice, double lineTotal){
        this.id = OrderItemId;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }
}
