package com.martishyn.productapi.controller;

import com.jayway.jsonpath.JsonPath;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
public class ProductsControllerTests {

    private static final String PRODUCTS_URL = "/api/v1/products";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldReturnNotFoundWhenNoProducts() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(List.of());

        ResultActions requestResult = mockMvc.perform(get(PRODUCTS_URL));

        requestResult.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFoundProducts() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(
                List.of(new Product(1L, "test1", BigDecimal.ONE, "test1-category"),
                        new Product(2L, "test2", BigDecimal.TEN, "test2-category")));

        ResultActions requestResult = mockMvc.perform(get(PRODUCTS_URL));

        requestResult.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L)
                );
    }

    @Test
    void shouldReturnProductById() throws Exception {
        Mockito.when(productService.getProductById(1L)).thenReturn(
                new Product(1L, "test1", BigDecimal.ONE, "test1-category"));

        mockMvc.perform(get(PRODUCTS_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.category").value("test1-category"));
    }

    @Test
    void shouldReturnNotFoundWhenProductNotFound() throws Exception {
        Mockito.when(productService.getProductById(1L)).thenReturn(null);

        mockMvc.perform(get(PRODUCTS_URL + "/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProductFromRequest() throws Exception {
        String productJson = """
                {
                   "id": 1,
                  "name": "Laptop",
                  "price": 1299.99,
                  "category": "Electronics"
                }
                """;
        Product product = new Product(1L, "Laptop", BigDecimal.valueOf(1299.99), "Electronics");
        Mockito.when(productService.createProduct(product)).thenReturn(product);

        mockMvc.perform(post(PRODUCTS_URL).content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(header().string("Location", "/api/v1/products/1"));

    }


}



