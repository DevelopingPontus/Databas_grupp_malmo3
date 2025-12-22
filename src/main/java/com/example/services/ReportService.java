package com.example.services;

import com.example.models.OrderItem;
import com.example.models.Orders;
import com.example.models.Product;
import com.example.respoitories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;


    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Map<Product, Integer> getTopSellingProducts(int limit) {
        List<Orders> orders = orderRepository.findAll();

        orders.forEach(order -> order.getOrderItems().size());

        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                        Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }


    @Transactional
    public List<Map.Entry<Product, Integer>> getLowStockProducts(int threshold) {
        List<Orders> orders = orderRepository.findAll();
        orders.forEach(order -> order.getOrderItems().size());

        return orderRepository.findAll().stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProduct)
                .distinct()
                .filter(product -> product.getInventory() != null)
                .filter(product -> product.getInventory().getQuantity() < threshold)
                .map(product -> Map.entry(product, product.getInventory().getQuantity()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Double> getDailyRevenue(int days) {

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        return orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt() != null)
                .filter(order -> {
                    LocalDateTime created = order.getCreatedAt().toLocalDateTime();
                    return !created.isBefore(startDate) && !created.isAfter(endDate);
                })
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedAt().toLocalDateTime().toLocalDate().toString(),
                        Collectors.summingDouble(order -> order.getTotal().doubleValue())
                ));
    }
}