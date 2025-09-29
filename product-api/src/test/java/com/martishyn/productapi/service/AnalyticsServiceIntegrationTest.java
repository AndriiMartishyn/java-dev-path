package com.martishyn.productapi.service;

import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.repository.CustomerRepository;
import com.martishyn.productapi.repository.OrderRepository;
import com.martishyn.productapi.repository.PaymentRepository;
import com.martishyn.productapi.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUtil;
import org.hibernate.jpa.internal.util.PersistenceUtilHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class AnalyticsServiceIntegrationTest {

    @Autowired
    private AnalyticsService analyticsService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DirtiesContext
    void testCountOrdersByCustomer() {
        Customer customer1 = customerRepository.save(new Customer("Andrii", "test@gmail.com"));
        Customer customer2 = customerRepository.save(new Customer("Kira", "test1@gmail.com"));

        orderRepository.save(new Order(customer1));
        orderRepository.save(new Order(customer1));
        orderRepository.save(new Order(customer2));

        Map<Long, Long> customerToOrderCount = analyticsService.countOrderByCustomers();
        Assertions.assertEquals(1, customerToOrderCount.get(2L));
        Assertions.assertEquals(2, customerToOrderCount.get(1L));
    }

    @Test
    @DirtiesContext
    void findOrdersWithoutPayments(){
        Customer c1 = customerRepository.save(new Customer("Charlie", "charlie@mail.com"));
        Order order1 = orderRepository.save(new Order(c1));
        Order order2 = orderRepository.save(new Order(c1));
        paymentRepository.save(new Payment(BigDecimal.valueOf(100L), PaymentStatus.PENDING, order1));

        List<Order> ordersWithoutPayment = analyticsService.findOrdersWithoutPayment();

        Assertions.assertEquals(1, ordersWithoutPayment.size());
        Assertions.assertEquals(ordersWithoutPayment.get(0).getId(), order2.getId());
    }
}
