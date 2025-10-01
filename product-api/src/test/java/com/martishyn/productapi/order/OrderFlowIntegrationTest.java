package com.martishyn.productapi.order;


import com.martishyn.productapi.dto.CreateCustomerRequest;
import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.exceptions.OrderNotFoundException;
import com.martishyn.productapi.exceptions.PaymentNotFoundException;
import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.CategoryRepository;
import com.martishyn.productapi.repository.CustomerRepository;
import com.martishyn.productapi.repository.OrderRepository;
import com.martishyn.productapi.repository.PaymentRepository;
import com.martishyn.productapi.repository.ProductRepository;
import com.martishyn.productapi.service.CustomerService;
import com.martishyn.productapi.service.OrderService;
import com.martishyn.productapi.service.PaymentService;
import com.martishyn.productapi.service.ProductService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderFlowIntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    private Customer customer;
    private Customer customerWithoutOrders;
    private Product product1;
    private Product product2;
    private Category category;

    @BeforeEach
    void setup() {
        category = categoryRepository.save(new Category("Electronics"));
        customer = customerRepository.save(new Customer("Alice", "alice@mail.com"));
        customerWithoutOrders = customerRepository.save(new Customer("Kira", "test@gmail.com"));
        product1 = productRepository.save(new Product("Laptop", BigDecimal.valueOf(1200), category));
        product2 = productRepository.save(new Product("Phone", BigDecimal.valueOf(800), category));
    }


    @Test
    void createOrder_shouldPersistOrderAndPayment() {
        Order createdOrder = orderService.createOrder(customer.getId(), List.of(product1.getId(), product2.getId()));

        Payment payment = paymentRepository.findById(createdOrder.getPayment().getId()).get();

        Assertions.assertEquals(2, createdOrder.getProducts().size());
        Assertions.assertTrue(createdOrder.getProducts().contains(product1));
        Assertions.assertTrue(createdOrder.getProducts().contains(product2));
        Assertions.assertEquals(createdOrder.getPayment().getId(), payment.getId());
        Assertions.assertEquals(createdOrder.getPayment().getAmount(), createdOrder.getProducts().stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        Assertions.assertEquals(PaymentStatus.PENDING, createdOrder.getPayment().getStatus());
    }

    @Test
    void getCustomersOrders_shouldReturnOrdersForCustomer() {
        Order createdOrder = orderService.createOrder(customer.getId(), List.of(product1.getId(), product2.getId()));

        Set<Order> customersOrders = orderService.getCustomersOrders(customer.getId());
        Set<Product> productsFromOrder = customersOrders.stream().map(Order::getProducts).findAny().orElse(new HashSet<>());
        Assertions.assertEquals(1, customersOrders.size());
        Assertions.assertEquals(2, productsFromOrder.size());
        Assertions.assertTrue(productsFromOrder.contains(product1));
        Assertions.assertTrue(productsFromOrder.contains(product2));

        Assertions.assertEquals(0, orderService.getCustomersOrders(customerWithoutOrders.getId()).size());
    }

    @Test
    void getOrderWithProducts_shouldReturnOrderWithProducts() {
        Order createdOrder = orderService.createOrder(customer.getId(), List.of(product1.getId(), product2.getId()));

        Order orderWithProducts = orderService.getOrderWithProducts(createdOrder.getId());
        Assertions.assertEquals(createdOrder.getId(), orderWithProducts.getId());
        Assertions.assertEquals(2, orderWithProducts.getProducts().size());
        Assertions.assertTrue(orderWithProducts.getProducts().contains(product1));
        Assertions.assertTrue(orderWithProducts.getProducts().contains(product2));

        Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderWithProducts(Long.MAX_VALUE), "Order not found");
    }

    @Test
    void updateOrderProducts_shouldReplaceProducts() {

        Product product3 = productRepository.save(new Product("Smartphone", BigDecimal.valueOf(199.99), category));
        Product product4 = productRepository.save(new Product("Tablet", BigDecimal.valueOf(149.99), category));
        Product product5 = productRepository.save(new Product("Headphones", BigDecimal.valueOf(19.99), category));

        Order createdOrder = orderService.createOrder(customer.getId(), List.of(product1.getId(), product2.getId()));
        Assertions.assertEquals(2, createdOrder.getProducts().size());
        Assertions.assertTrue(createdOrder.getProducts().contains(product1));
        Assertions.assertTrue(createdOrder.getProducts().contains(product2));

        Order updatedOrderWithProducts = orderService.updateOrderProducts(createdOrder.getId(), List.of(product3.getId(), product4.getId(), product5.getId()));
        Assertions.assertEquals(createdOrder.getId(), updatedOrderWithProducts.getId());
        Assertions.assertEquals(3, updatedOrderWithProducts.getProducts().size());
        Assertions.assertTrue(updatedOrderWithProducts.getProducts().contains(product3));
        Assertions.assertTrue(updatedOrderWithProducts.getProducts().contains(product4));
        Assertions.assertTrue(updatedOrderWithProducts.getProducts().contains(product5));
        Assertions.assertFalse(updatedOrderWithProducts.getProducts().contains(product1));
        Assertions.assertFalse(updatedOrderWithProducts.getProducts().contains(product2));
    }

    @Test
    void deleteOrder_shouldRemoveOrderAndPayment() {
        Order createdOrder = orderService.createOrder(customer.getId(), List.of(product1.getId(), product2.getId()));

        orderService.deleteOrder(createdOrder.getId());

        productRepository.findById(product1.getId()).ifPresent(product -> Assertions.assertFalse(product.getOrders().contains(createdOrder)));
        productRepository.findById(product2.getId()).ifPresent(product -> Assertions.assertFalse(product.getOrders().contains(createdOrder)));
        Assertions.assertFalse(paymentRepository.findById(createdOrder.getPayment().getId()).isPresent());
    }

    @Test
    void deleteOrder_shouldThrowExceptionWhenOrderNotFound() {
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(Long.MAX_VALUE));
    }
}
