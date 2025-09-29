package com.martishyn.productapi.controller;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.model.Category;
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
        Mockito.when(productService.findAllProducts()).thenReturn(List.of());

        ResultActions requestResult = mockMvc.perform(get(PRODUCTS_URL));

        requestResult.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnFoundProducts() throws Exception {
        Mockito.when(
                productService.findAllProducts()).thenReturn(
                List.of(new Product(1L, "test1", BigDecimal.ONE, new Category("test1-category")),
                        new Product(2L, "test2", BigDecimal.TEN, new Category("test2-category"))));

        ResultActions requestResult = mockMvc.perform(get(PRODUCTS_URL));

        requestResult.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L)
                );
    }

    @Test
    void shouldReturnProductById() throws Exception {
        Mockito.when(productService.findProductById(1L)).thenReturn(
                new Product(1L, "test1", BigDecimal.ONE, new Category("test1-category")));

        mockMvc.perform(get(PRODUCTS_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test1"))
                .andExpect(jsonPath("$.price").value(1.0))
                .andExpect(jsonPath("$.category.name").value("test1-category"));
    }

    @Test
    void shouldReturnNotFoundWhenProductNotFound() throws Exception {
        Mockito.when(productService.findProductById(1L)).thenReturn(null);

        mockMvc.perform(get(PRODUCTS_URL + "/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProductFromRequest() throws Exception {
        String productJson = """
                {
                  "name": "Laptop",
                  "price": 1299.99
                }
                """;
        Category category = new Category(1L, "Electronics");
        ProductCreateRequest newProduct = new ProductCreateRequest("Laptop", BigDecimal.valueOf(1299.99));
        Product createdProduct = new Product(1L, "Laptop", BigDecimal.valueOf(1299.99), category);
        Mockito.when(productService.createProduct(newProduct, 1L)).thenReturn(createdProduct);

        final ResultActions perform = mockMvc.perform(post(PRODUCTS_URL + "/{categoryId}", 1L)
                .content(productJson)
                .contentType("application/json"));
        perform
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.category.name").value("Electronics"))
                .andExpect(header().string("Location", "/api/v1/products/1"));
    }

    @Test
    void shouldUpdatePassedProduct() throws Exception {
        Category category = new Category(1L, "Electronics");
        String productJson = """
                {
                   "id": 1,
                  "name": "Laptop",
                  "price": 1299.99,
                  "categoryId": 1
                }
                """;
        ProductUpdateRequest product = new ProductUpdateRequest(1L, "Laptop", BigDecimal.valueOf(1299.99), 1L);
        Product updatedProduct = new Product(1L, "Laptop1", BigDecimal.valueOf(1299.99), category);
        Mockito.when(productService.updateProduct(product)).thenReturn(updatedProduct);

        mockMvc.perform(put(PRODUCTS_URL + "/{productId}", 1L)
                        .content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop1"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.category.name").value("Electronics"));
    }

    @Test
    void shouldReturnNotFoundWhenTryingToUpdateNotExistingProduct() throws Exception {
        String productJson = """
                {
                   "id": 1,
                  "name": "Laptop",
                  "price": 1299.99,
                  "categoryId" : 1
                }
                """;
        ProductUpdateRequest product = new ProductUpdateRequest(1L, "Laptop", BigDecimal.valueOf(1299.99), 1L);
        Mockito.when(productService.updateProduct(product)).thenReturn(null);

        mockMvc.perform(put(PRODUCTS_URL + "/{productId}", 1L)
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
                  "category": 1
                }
                """;
        mockMvc.perform(put(PRODUCTS_URL + "/{productId}", 1L)
                        .content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].name").value("must not be blank"));
    }

    @Test
    void shouldDeleteProductById() throws Exception {
        Mockito.doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete(PRODUCTS_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(productService, Mockito.times(1)).deleteProductById(1L);
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    void shouldNotCreateProductWhenValidationFails() throws Exception {
        String productJson = """
                {
                  "name": "Laptop",
                  "price": -10.00,
                  "categoryId" : 1
                }
                """;

        mockMvc.perform(post(PRODUCTS_URL + "/{categoryId}", 1L)
                        .content(productJson)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}



