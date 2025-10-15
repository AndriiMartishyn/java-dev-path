package com.martishyn.productapi.controller;

import com.martishyn.productapi.enums.PaymentStatus;
import com.martishyn.productapi.model.Payment;
import com.martishyn.productapi.repository.PaymentRepository;
import com.martishyn.productapi.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTests {

    private static final String PAYMENTS_URL = "/api/v1/payments";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Spy
    private Payment mockPayment1;

    @BeforeEach
    void setUp() {
        when(mockPayment1.getId()).thenReturn(1L);

    }

    @Test
    void shouldNotCreatePaymentWhenPassingInvalidOrderId() throws Exception {
        mockMvc.perform(post(PAYMENTS_URL + "/"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post(PAYMENTS_URL + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreatePaymentAndReturnBuildUriWithNormalInput() throws Exception {
        when(paymentService.createPayment(1L, List.of(1L, 2L, 3L))).thenReturn(mockPayment1);
        mockMvc.perform(post(PAYMENTS_URL + "/{orderId}", 1L)
                        .param("productIds", "1")
                        .param("productIds", "2")
                        .param("productIds", "3"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(paymentService, Mockito.times(1)).createPayment(1L, List.of(1L,2L,3L));
    }

    @Test
    void shouldSetPaymentStatusWhenPaymentWithNormalInput() throws Exception {
        when(paymentService.findPaymentById(1L)).thenReturn(mockPayment1);
        when(paymentService.updatePaymentStatus(1L, PaymentStatus.PAID)).thenReturn(mockPayment1);
        mockMvc.perform(put(PAYMENTS_URL + "/{orderId}", 1L)
                        .param("status", PaymentStatus.PAID.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value(PaymentStatus.PAID.name()));


        verify(paymentService, Mockito.times(1)).findPaymentById(1L);
        verify(paymentService, Mockito.times(1)).updatePaymentStatus(1L, PaymentStatus.PAID);
    }

    @Test
    void shouldGetPaymentForOrderByOrderId() throws Exception {
        when(paymentService.getPaymentForOrder(1L)).thenReturn(mockPayment1);
        mockMvc.perform(get(PAYMENTS_URL + "/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(paymentService, Mockito.times(1)).getPaymentForOrder(1L);
    }
}
