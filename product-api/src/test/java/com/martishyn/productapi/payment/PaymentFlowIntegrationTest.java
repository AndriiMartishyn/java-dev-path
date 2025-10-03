package com.martishyn.productapi.payment;

import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.exceptions.PaymentNotFoundException;
import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.CategoryRepository;
import com.martishyn.productapi.repository.CustomerRepository;
import com.martishyn.productapi.repository.PaymentRepository;
import com.martishyn.productapi.repository.ProductRepository;
import com.martishyn.productapi.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Set;

@SpringBootTest
@Testcontainers
@Transactional
public class PaymentFlowIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Customer customer;
    private Order order;
    private Set<Product> products;

    @BeforeEach
    void setup() {
        Category category = categoryRepository.save(new Category("Electronics"));
        customer = customerRepository.save(new Customer("Alice", "alice@mail.com"));
        Product product1 = productRepository.save(new Product("Laptop", BigDecimal.valueOf(1200), category));
        Product product2 = productRepository.save(new Product("Phone", BigDecimal.valueOf(800), category));
        products = Set.of(product1, product2);
        order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
    }


    @Test
    void shouldCreatePayment() {
        Payment createdPayment = paymentService.createPayment(order, products);

        Assertions.assertNotNull(createdPayment.getId());
        Assertions.assertSame(PaymentStatus.PENDING, createdPayment.getStatus());
        Assertions.assertEquals(new BigDecimal("2000"), createdPayment.getAmount());

        Payment persistedPayment = paymentRepository.findById(createdPayment.getId()).orElse(null);
        Assertions.assertNotNull(persistedPayment);
        Assertions.assertEquals(createdPayment.getId(), persistedPayment.getId());
        Assertions.assertEquals(createdPayment.getStatus(), persistedPayment.getStatus());
        Assertions.assertEquals(new BigDecimal("2000"), persistedPayment.getAmount());
    }

    @Test
    void shouldUpdatePaymentStatus() {
        Payment createdPayment = paymentService.createPayment(order, products);

        Payment updatedPayment = paymentService.updatePaymentStatus(createdPayment.getId(), PaymentStatus.PAID);
        Assertions.assertEquals(PaymentStatus.PAID, updatedPayment.getStatus());

        Payment persistedPayment = paymentRepository.findById(createdPayment.getId()).orElse(null);
        Assertions.assertNotNull(persistedPayment);
        Assertions.assertEquals(PaymentStatus.PAID, persistedPayment.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFoundToUpdateStatus() {
        Assertions.assertThrows(PaymentNotFoundException.class, () -> paymentService.updatePaymentStatus(Long.MAX_VALUE, PaymentStatus.PAID));
    }

    @Test
    void shouldThrowExceptionWhenPassedWrongPaymentStatus() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(Long.MAX_VALUE, PaymentStatus.valueOf("test")));
    }

    @Test
    void shouldGetPaymentForOrder() {
        Payment createdPayment = paymentService.createPayment(order, products);

        Payment fetchedPayment = paymentService.getPaymentForOrder(createdPayment.getOrder().getId());
        Assertions.assertNotNull(fetchedPayment);
        Assertions.assertEquals(order.getId(), fetchedPayment.getOrder().getId());
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFoundForOrder() {
        Assertions.assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentForOrder(Long.MAX_VALUE));
    }
}
