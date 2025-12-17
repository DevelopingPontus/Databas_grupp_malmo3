package com.example.models;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;
    @Column(name = "line_total", nullable = false)
    private BigDecimal lineTotal;

    // Relations
    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    private Product product;

    // Constructor
    public OrderItem() {
    }

    public OrderItem(int quantity, BigDecimal unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    @Embeddable
    class OrderItemId implements java.io.Serializable {
        @Column(name = "order_id")
        private int order_id;
        @Column(name = "product_id")
        private int product_id;

        public OrderItemId() {
        }

        public OrderItemId(int order_id, int product_id) {
            this.order_id = order_id;
            this.product_id = product_id;
        }
    }
}
