package com.martishyn.productapi.service;

import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.exceptions.PaymentNotFoundException;
import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createPayment(Order order, Set<Product> products) {
        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new IllegalStateException("Payment already exists for order " + order.getId());
        }
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        BigDecimal productsPrice = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        payment.setOrder(order);
        payment.setAmount(productsPrice);
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("Payment id " + paymentId + " not found"));
        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Payment is already completed");
        }
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment getPaymentForOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(() -> new PaymentNotFoundException("Payment for orderId " + orderId + " not found"));
    }
}
