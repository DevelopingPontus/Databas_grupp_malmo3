package com.example.respoitories;

import com.example.models.OrderItem;
import com.example.models.Orders;
import com.example.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> findByOrderAndProduct(Orders order, Product product);

}
