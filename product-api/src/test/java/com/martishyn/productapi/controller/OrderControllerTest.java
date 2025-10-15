package com.martishyn.productapi.controller;

import com.martishyn.productapi.model.Order;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    private static final String ORDERS_URL = "/api/v1/orders";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Spy
    private Order order;

    @Test
    void shouldCreateOrderWhenPassingValidCustomerAndProductsIds() throws Exception {
        Product product1 = new Product(1L, "test1", null, null);
        Product product2 = new Product(2L, "test2", null, null);
        Set<Product> products = new LinkedHashSet<>(List.of(product1, product2));
        order = new Order(1L, null, products, null);
        Mockito.when(orderService.createOrder(1L, List.of(1L, 2L))).thenReturn(order);

        mockMvc.perform(post(ORDERS_URL + "/{customerId}", 1L)
                        .param("productIds", "1")
                        .param("productIds", "2"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/orders/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.products[0].id").value(1L))
                .andExpect(jsonPath("$.products[1].id").value(2L));
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsNegative() throws Exception {
        mockMvc.perform(post(ORDERS_URL + "/{customerId}", -1L)
                .param("productIds", "1")
                .param("productIds", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]['createOrder.customerId']").value("must be greater than 0"));
    }

    @Test
    void shouldReturnOrderByIdWhenPassingValidData() throws Exception {
        Product product1 = new Product(1L, "test1", null, null);
        Product product2 = new Product(2L, "test2", null, null);
        Set<Product> products = new LinkedHashSet<>(List.of(product1, product2));
        order = new Order(1L, null, products, null);
        Mockito.when(orderService.getOrderWithProducts(1L)).thenReturn(order);

        mockMvc.perform(get(ORDERS_URL + "/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.products[0].id").value(1L))
                .andExpect(jsonPath("$.products[1].id").value(2L));
    }

    @Test
    void shouldReturnBadRequestWhenOrderIdIsInvalid() throws Exception {
        mockMvc.perform(get(ORDERS_URL + "/"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get(ORDERS_URL + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowValidationErrorWhenGettingOrderWithNegativeOrderId() throws Exception {
        mockMvc.perform(get(ORDERS_URL + "/{orderId}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]['getOrderById.orderId']").value("must be greater than 0"));
    }

    @Test
    void shouldThrowValidationErrorWhenDeleteOrderWithNegativeOrderId() throws Exception {
        mockMvc.perform(delete(ORDERS_URL + "/{orderId}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]['deleteOrder.orderId']").value("must be greater than 0"));
    }

    @Test
    void shouldReturnNoContentWhenDeleteOrder() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete(ORDERS_URL + "/{orderId}", 1L))
                .andExpect(status().isNoContent());
        verify(orderService, Mockito.times(1)).deleteOrder(1L);
    }
}
