package com.martishyn.productapi.service;

import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.exceptions.PaymentNotFoundException;
import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.PaymentRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final ProductService productService;

    public PaymentService(PaymentRepository paymentRepository, @Lazy OrderService orderService, ProductService productService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.productService = productService;
    }

    @Transactional
    public Payment createPayment(Long orderId, List<Long> productIds) {
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment.isPresent()) {
            throw new IllegalStateException("Payment already exists for order " + orderId);
        }
        Order orderWithProducts = orderService.getOrderWithProducts(orderId);
        Payment newPayment = new Payment();
        newPayment.setStatus(PaymentStatus.PENDING);
        BigDecimal productsPrice = productService.findProductsByIds(productIds)
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        newPayment.setAmount(productsPrice);
        newPayment.setOrder(orderWithProducts);
        return paymentRepository.save(newPayment);
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
