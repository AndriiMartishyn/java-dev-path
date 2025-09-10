package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductRequestDto;
import com.martishyn.productapi.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceTest {

    private  ProductService productService;

    private  List<Product> products;

    @BeforeEach
     void contextLoads() {
        Product product = new Product(1L, "product1", BigDecimal.valueOf(11.00), "category1");
        Product product2 = new Product(2L, "product2", BigDecimal.valueOf(22.00), "category2");
        Product product3 = new Product(3L, "product3", BigDecimal.valueOf(33.00), "category3");
        Product product4 = new Product(4L, "product4", BigDecimal.valueOf(44.00), "category4");
        Product product5 = new Product(5L, "product5", BigDecimal.valueOf(55.00), "category5");
        products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);
        productService = new InMemoryProductService(products);
    }

    @Test
    void shouldReturnAllProducts() {
        List<Product> allProducts = productService.getAllProducts();

        Assertions.assertEquals(products.size(), allProducts.size());
        Assertions.assertEquals(products.get(0).getId(), allProducts.get(0).getId());
        Assertions.assertEquals(products.get(0).getName(), allProducts.get(0).getName());
        Assertions.assertEquals(products.get(0).getPrice(), allProducts.get(0).getPrice());
        Assertions.assertEquals(products.get(0).getCategory(), allProducts.get(0).getCategory());
    }

    @Test
    void shouldReturnProductById() {
        Product product = productService.getProductById(products.get(0).getId());

        Assertions.assertEquals(products.get(0).getId(), product.getId());
    }

    @Test
    void shouldCreateProductAndGenerateId() {
        ProductRequestDto newProduct = new ProductRequestDto("newProduct", BigDecimal.valueOf(100.00), "newCategory");
        ProductRequestDto newProduct1 = new ProductRequestDto("newProduct2", BigDecimal.valueOf(100.00), "newCategory");
        Product createdProduct1 = productService.createProduct(newProduct);
        Product createdProduct2 = productService.createProduct(newProduct1);

        Assertions.assertEquals(7, products.size());
        Assertions.assertEquals(1L, createdProduct1.getId());
        Assertions.assertEquals(2L, createdProduct2.getId());
    }

    @Test
    void shouldUpdateProduct() {
        ProductRequestDto product = new ProductRequestDto(1L, "product-updated", BigDecimal.valueOf(12.00), "category-updated");

        productService.updateProduct(product);

        Assertions.assertEquals(product.getName(), products.get(0).getName());
        Assertions.assertEquals(product.getPrice(), products.get(0).getPrice());
        Assertions.assertEquals(product.getCategory(), products.get(0).getCategory());
    }

    @Test
    void shouldDeleteProduct() {
        productService.deleteProduct(1L);
        Product product = productService.getAllProducts()
                .stream().filter(p -> p.getId().equals(1L))
                .findFirst().orElse(null);
        Assertions.assertEquals(4, products.size());
        Assertions.assertNull(product);
    }
}
