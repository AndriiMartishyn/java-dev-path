package com.martishyn.productapi.controller;

import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.service.PaymentService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping(value = "/{orderId}")
    public ResponseEntity<?> createPayment(@PathVariable @Positive @NotNull Long orderId, @RequestParam List<Long> productIds) {
        Payment payment = paymentService.createPayment(orderId, productIds);
        URI uri = UriComponentsBuilder.fromPath("/api/v1/payments/{id}")
                .buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(uri).body(payment);
    }

    @PutMapping(value = "/{paymentId}")
    public ResponseEntity<?> setPaymentStatus(@PathVariable @NotNull @Positive Long paymentId, @RequestParam PaymentStatus status) {
        Payment foundPayment = paymentService.findPaymentById(paymentId);
        foundPayment.setStatus(status);
        Payment updatedPayment = paymentService.updatePaymentStatus(foundPayment.getId(), status);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<?> getPaymentForOrder(@PathVariable @Positive @NotNull Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentForOrder(orderId));
    }
}
