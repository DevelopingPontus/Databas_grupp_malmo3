package com.example.services;

import com.example.models.Payment;
import com.example.respoitories.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public CartService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Payment getPaymentStatusById(int id) {
        return paymentRepository.findPaymentStatusById(id);
    }

}
