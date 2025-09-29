package com.martishyn.productapi.order;


import com.martishyn.productapi.dto.CreateCustomerRequest;
import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.exceptions.OrderNotFoundException;
import com.martishyn.productapi.exceptions.PaymentNotFoundException;
import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.CategoryRepository;
import com.martishyn.productapi.service.CustomerService;
import com.martishyn.productapi.service.OrderService;
import com.martishyn.productapi.service.PaymentService;
import com.martishyn.productapi.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class OrderFlowIntegrationTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentService paymentService;

    @Test
    void shouldCreateCustomerOrderAndPayment() {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("Andrii", "test@gmail.com");
        Customer customer = customerService.createCustomer(createCustomerRequest);

        Category category = new Category("Games");
        Category savedCategory = categoryRepository.save(category);

        ProductCreateRequest productCreateRequest = new ProductCreateRequest("Playstation 6", BigDecimal.valueOf(400));
        ProductCreateRequest productCreateRequest2 = new ProductCreateRequest("Playstation 5", BigDecimal.valueOf(360));

        Product createdProduct = productService.createProduct(productCreateRequest, savedCategory.getId());
        Product createdProduct2 = productService.createProduct(productCreateRequest2, savedCategory.getId());

        Order order = orderService.createOrder(customer.getId(), List.of(createdProduct.getId(), createdProduct2.getId()));

        Assertions.assertNotNull(customer.getId());
        Assertions.assertNotNull(createdProduct.getId());
        Assertions.assertNotNull(createdProduct2.getId());


        Assertions.assertNotNull(order);
        Assertions.assertEquals(PaymentStatus.PENDING, order.getPayment().getStatus());
        Assertions.assertEquals(BigDecimal.valueOf(760), order.getPayment().getAmount());
        Assertions.assertEquals(2, order.getProducts().size());
        Customer orderCustomer = order.getCustomer();
        Assertions.assertNotNull(orderCustomer);
        Assertions.assertEquals(customer.getId(), orderCustomer.getId());
        Assertions.assertEquals(customer.getName(), orderCustomer.getName());
        Assertions.assertEquals(customer.getEmail(), orderCustomer.getEmail());


        customerService.deleteCustomer(customer.getId());
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrderWithProducts(order.getId()));
        Assertions.assertThrows(PaymentNotFoundException.class, () ->  paymentService.getPaymentForOrder(order.getId()));
    }

}
