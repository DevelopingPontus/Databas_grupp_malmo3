package com.example.respoitories;

import com.example.models.Customer;
import com.example.models.Orders;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    Optional<Orders> findByCustomerAndStatus(Customer customer, Orders.OrderStatus status);
    Optional<Orders> findFirstByCustomerIdAndStatusOrderByCreatedAtDesc(
            Integer customerId,
            Orders.OrderStatus status
    );

}
