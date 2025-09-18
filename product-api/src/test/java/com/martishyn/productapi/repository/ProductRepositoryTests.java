package com.martishyn.productapi.repository;

import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldReturnAllProducts() {
        List<Product> products = productRepository.findAll();
        Assertions.assertNotNull(products);
        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals(20, products.size());
    }

    @Test
    void shouldReturnProductById() {
        Product product = productRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(product);
        Assertions.assertEquals(1L, product.getId());
        Assertions.assertEquals("Laptop", product.getName());
    }

    @Test
    @DirtiesContext
    void shouldCreateNewProductInDatabase() {
        Product lastInsertedProduct = productRepository.findAll()
                .stream()
                .max(Comparator.comparing(Product::getId))
                .orElse(null);
        Product product = new Product();
        product.setName("TEST_NAME");
        product.setCategory("TEST_CATEGORY");
        product.setPrice(BigDecimal.ONE);
        productRepository.save(product);
        long insertedId = product.getId();
        Assertions.assertEquals(lastInsertedProduct.getId() + 1, insertedId);
    }

    @Test
    void shouldFindProductsByCategory() {
        List<Product> products = productRepository.findByCategory("Books");
        Assertions.assertEquals(10, products.size());
    }

    @Test
    void shouldFindProductsByPriceBetween() {
        List<Product> products = productRepository.findByPriceBetween(BigDecimal.valueOf(10), BigDecimal.valueOf(100));
        Assertions.assertEquals(12, products.size());
    }

    @Test
    void shouldFindProductsByCategoryAndPriceLessThan() {
        List<ProductResponseDto> products = productRepository.findByCategoryAndPriceLessThan("Electronics",  BigDecimal.valueOf(900));
        Assertions.assertEquals(8, products.size());
    }
}
