//package com.example.models;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "order_item")
//public class OrderItem {
//    //Attributes
//    //Denna tabell har egen id i KRAV (MVP) och i sql schema. Men vet ej om det är rätt för att OrderItems är typ
//    //junktion table ska vi därför använda @Embeddable?
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private int quantity;
//    private double unitPrice;
//    private double lineTotal;
//
//    // Relations
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;
//
//    //Constructor
//    public OrderItem() {
//    }
//
//    public OrderItem(int quantity, double unitPrice, double lineTotal) {
//        this.order = order;
//        this.product = product;
//        this.quantity = quantity;
//        this.unitPrice = unitPrice;
//        this.lineTotal = lineTotal;
//    }
//
//    @Embeddable
//    class OrderItemId implements java.io.Serializable {
//        @Column(name = "order_id")
//        private Long order_id;
//        @Column(name = "product_id")
//        private Long product_id;
//
//        public OrderItemId() {
//        }
//
//        public OrderItemId(Long order_id, Long product_id) {
//            this.order_id = order_id;
//            this.product_id = product_id;
//        }
//    }
//}
