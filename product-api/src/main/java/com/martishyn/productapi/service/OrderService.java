package com.martishyn.productapi.service;

import com.martishyn.productapi.exceptions.OrderNotFoundException;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String ORDER_NOT_FOUND = "Order not found";

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final PaymentService paymentService;

    @Transactional
    public void createOrder(Long customerId, List<Long> productIds) {
        Customer foundCustomer = customerService.findCustomerById(customerId);
        Set<Product> foundProducts = productService.findProductsByIds(productIds);
        Order order = new Order();
        order.setCustomer(foundCustomer);
        order.setProducts(foundProducts);
        Order createdOrder = orderRepository.save(order);
        Payment createdPayment = paymentService.createPayment(createdOrder, foundProducts);
        order.setPayment(createdPayment);
        foundCustomer.setOrder(createdOrder);
        orderRepository.save(order);
    }

    public List<Order> getCustomersOrders(Long customerId) {
        Customer foundCustomer = customerService.findCustomerById(customerId);
        return foundCustomer.getOrders();
    }

    public Order getOrderWithProducts(Long orderId) {
        return orderRepository.findWithProducts(orderId).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
    }

    public void deleteOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException(ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }

    public Order updateOrderProducts(Long orderId, List<Long> productIds) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        order.getProducts().forEach(order::removeProduct);
        Set<Product> foundProducts = productService.findProductsByIds(productIds);
        foundProducts.forEach(order::setProduct);
        return orderRepository.save(order);
    }


}

