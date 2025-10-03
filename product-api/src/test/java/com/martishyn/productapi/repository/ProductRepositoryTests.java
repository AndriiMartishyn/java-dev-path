package com.martishyn.productapi.repository;

import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void connectionEstablished() {
        Assertions.assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    void shouldSaveProductWithCategory() {
        Category category = new Category("tv");
        categoryRepository.save(category);

        Product product = new Product("LG", BigDecimal.valueOf(150L), category);
        productRepository.save(product);

        List<Product> productsByCategory = productRepository.findProductsByCategory(category);
        Assertions.assertEquals(1, productsByCategory.size());
        Assertions.assertEquals("LG", productsByCategory.get(0).getName());
    }

    @Test
    void shouldDeleteProductCascadingWhenCategoryIsDeleted() {
        Category category = new Category("tv");

        Product product = new Product("LG", BigDecimal.valueOf(150L), category);
        category.addProduct(product);
        categoryRepository.save(category);

        int sizeAfterInsertion = productRepository.findAll().size();

        categoryRepository.delete(category);

        int sizeAfterDeletion = productRepository.findAll().size();

        Assertions.assertEquals(sizeAfterInsertion - 1, sizeAfterDeletion);
        Assertions.assertFalse(productRepository.findAll().contains(product));
    }

    @Test
    void shouldReturnAllProducts() {
        List<Product> products = productRepository.findAll();
        Assertions.assertNotNull(products);
        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals(50, products.size());
    }

    @Test
    void shouldReturnProductById() {
        Product product = productRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(product);
        Assertions.assertEquals(1L, product.getId());
        Assertions.assertEquals("Laptop", product.getName());
    }

    @Test
    void shouldFindProductsByCategory() {
        Category category = categoryRepository.findByName("Books").orElse(null);
        List<Product> products = productRepository.findProductsByCategory(category);
        Assertions.assertEquals(10, products.size());
    }

    @Test
    void shouldFindProductsByPriceBetween() {
        List<Product> products = productRepository.findByPriceBetween(BigDecimal.valueOf(10), BigDecimal.valueOf(100));
        Assertions.assertEquals(23, products.size());
    }

    @Test
    void shouldFindProductsByCategoryAndPriceLessThan() {
        Category category = categoryRepository.findByName("Electronics").orElse(null);
        List<Product> products = productRepository.findProductByCategoryAndPriceLessThan(category, BigDecimal.valueOf(900));
        Assertions.assertEquals(8, products.size());
    }
}
