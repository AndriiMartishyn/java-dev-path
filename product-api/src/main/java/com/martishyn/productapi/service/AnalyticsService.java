package com.martishyn.productapi.service;

import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.repository.OrderRepository;
import com.martishyn.productapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Literal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public Map<Long, Long> countOrderByCustomers() {
        return orderRepository.countOrdersByCustomer()
                .stream().collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    @Transactional(readOnly = true)
    public Object findCustomerWithHighestPayments() {
        return paymentRepository.findCustomerWithHighestPayments();
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersWithoutPayment() {
        return orderRepository.findAllOrdersWithoutPayment();
    }
}
