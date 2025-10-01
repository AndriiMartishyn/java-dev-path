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

import java.util.ArrayList;
import java.util.HashSet;
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
    public Order createOrder(Long customerId, List<Long> productIds) {
        Customer foundCustomer = customerService.findCustomerById(customerId);
        Set<Product> foundProducts = productService.findProductsByIds(productIds);
        Order order = new Order();
        order.setProducts(foundProducts);
        foundCustomer.addOrder(order);
        Order createdOrder = orderRepository.save(order);
        Payment createdPayment = paymentService.createPayment(createdOrder, foundProducts);
        order.setPayment(createdPayment);
        return order;
    }

    @Transactional(readOnly = true)
    public Set<Order> getCustomersOrders(Long customerId) {
        Customer foundCustomer = customerService.findCustomerById(customerId);
        return foundCustomer.getOrders();
    }

    @Transactional(readOnly = true)
    public Order getOrderWithProducts(Long orderId) {
        return orderRepository.findWithProducts(orderId).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        orderRepository.deleteById(order.getId());
    }

    @Transactional
    public Order updateOrderProducts(Long orderId, List<Long> productIds) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        List<Product> copyOfProducts = new ArrayList<>(order.getProducts());
        for (Product p : copyOfProducts){
            order.removeProduct(p);
        }
        Set<Product> foundProducts = productService.findProductsByIds(productIds);
        foundProducts.forEach(order::setProduct);
        return orderRepository.save(order);
    }
}
