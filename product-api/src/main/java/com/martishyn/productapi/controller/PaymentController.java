package com.martishyn.productapi.controller;

import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.service.PaymentService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createPayment(@Positive @NotNull Long orderId, List<Long> productIds) {
        Payment payment = paymentService.createPayment(orderId, productIds);
        URI uri = UriComponentsBuilder.fromPath("/api/v1/orders/{id}")
                .buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(uri).body(payment);
    }

    @PutMapping
    public void setPaymentStatus(Payment payment, PaymentStatus status) {
        payment.setStatus(status);
        paymentService.updatePaymentStatus(payment.getId(), status);
    }

    @GetMapping
    public ResponseEntity<?> getPaymentForOrder(@Positive @NotNull Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentForOrder(orderId));
    }
}
