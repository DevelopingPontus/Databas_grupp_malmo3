package com.example.services;

import com.example.models.Orders;
import com.example.models.Payment;
import com.example.respoitories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final Random random = new Random();

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment process(Orders order, Payment.PaymentMethod method) {

        Payment payment = new Payment();
        payment.setOrders(order);
        payment.setMethod(method);
        payment.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        boolean approved = random.nextInt(10) < 9;

        payment.setStatus(
                approved
                        ? Payment.PaymentStatus.APPROVED
                        : Payment.PaymentStatus.DECLINED
        );

        return paymentRepository.save(payment);
    }
}
