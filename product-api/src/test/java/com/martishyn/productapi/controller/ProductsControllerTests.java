package com.martishyn.productapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martishyn.productapi.dto.ProductRequestDto;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
                
                  "name": "Laptop",
                  "price": 1299.99,
                  "category": "Electronics"
                }
                """;
        ProductRequestDto productRequestDto = new ProductRequestDto("Laptop", BigDecimal.valueOf(1299.99), "Electronics");
        Product product = new Product(1L, "Laptop", BigDecimal.valueOf(1299.99), "Electronics");
        Mockito.when(productService.createProduct(productRequestDto)).thenReturn(product);

        mockMvc.perform(post(PRODUCTS_URL)
                        .content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(header().string("Location", "/api/v1/products/1"));
    }

    @Test
    void shouldUpdatePassedProduct() throws Exception {
        String productJson = """
                {
                   "id": 1,
                  "name": "Laptop",
                  "price": 1299.99,
                  "category": "Electronics"
                }
                """;
        ProductRequestDto productRequestDto = new ProductRequestDto(1L, "Laptop", BigDecimal.valueOf(1299.99), "Electronics");
        Product product = new Product(1L, "Laptop", BigDecimal.valueOf(1299.99), "Electronics");
        Mockito.when(productService.updateProduct(productRequestDto)).thenReturn(product);

        mockMvc.perform(put(PRODUCTS_URL)
                        .content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.category").value("Electronics"));
    }

    @Test
    void shouldReturnNotFoundWhenTryingToUpdateNotExistingProduct() throws Exception {
        String productJson = """
                {
                   "id": 1,
                  "name": "Laptop",
                  "price": 1299.99,
                  "category": "Electronics"
                }
                """;
        ProductRequestDto productRequestDto = new ProductRequestDto(1L, "Laptop", BigDecimal.valueOf(1299.99), "Electronics");
        Mockito.when(productService.updateProduct(productRequestDto)).thenReturn(null);

        mockMvc.perform(put(PRODUCTS_URL)
                        .content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldThrowValidationErrorWhenUpdatingWithEmptyNameFieldInBody() throws Exception {
        String productJson = """
                {
                   "id": 1,
                  "name": "",
                  "price": 1299.99,
                  "category": "Electronics"
                }
                """;
        mockMvc.perform(put(PRODUCTS_URL)
                        .content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].name").value("must not be blank"));
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete(PRODUCTS_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(productService, Mockito.times(1)).deleteProduct(1L);
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    void shouldNotCreateProductWhenValidationFails() throws Exception {
        String productJson = """
                {
                  "name": "Laptop",
                  "price": -10.00,
                  "category": ""
                }
                """;

        mockMvc.perform(post(PRODUCTS_URL).content(productJson).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}



