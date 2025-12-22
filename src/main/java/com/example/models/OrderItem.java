package com.example.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "line_total", insertable = false, updatable = false)
    @Generated
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
    private OrderItem() {
    }

    public OrderItem(int quantity, BigDecimal unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderItem(int quantity, BigDecimal unitPrice, Product product, Orders order) {
        this.id = new OrderItemId(order.getId(), product.getId());
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.product = product;
        this.order = order;
    }

    // Vera testar setter
    public void setOrderAndProduct(Orders order, Product product) {
        this.order = order;
        this.product = product;
        this.id = new OrderItemId(order.getId(), product.getId());
    }

    public OrderItemId getId() {
        return id;
    }

    public void setId(OrderItemId id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Embeddable
    public static class OrderItemId implements java.io.Serializable {
        @Column(name = "order_id")
        private Integer order_id;

        @Column(name = "product_id")
        private Integer product_id;

        public OrderItemId() {
        }

        public OrderItemId(Integer order_id, Integer product_id) {
            this.order_id = order_id;
            this.product_id = product_id;
        }

        public Integer getOrder_id() {
            return order_id;
        }

        public void setOrder_id(Integer order_id) {
            this.order_id = order_id;
        }

        public Integer getProduct_id() {
            return product_id;
        }

        public void setProduct_id(Integer product_id) {
            this.product_id = product_id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof OrderItemId that))
                return false;
            return Objects.equals(order_id, that.order_id) &&
                    Objects.equals(product_id, that.product_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(order_id, product_id);
        }
    }
}
