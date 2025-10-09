package com.martishyn.productapi.controller;

import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.service.OrderService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> createOrder(@PathVariable @NotNull @Positive Long customerId, @RequestParam List<Long> productIds) {
        Order order = orderService.createOrder(customerId, productIds);
        URI uri = UriComponentsBuilder.fromPath("/api/v1/orders/{id}")
                .buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(uri).body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable @NotNull @Positive Long orderId) {
        Order orderWithProducts = orderService.getOrderWithProducts(orderId);
        return ResponseEntity.ok(orderWithProducts);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable @NotNull @Positive Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
