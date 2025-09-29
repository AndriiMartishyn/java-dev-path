package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.exceptions.ProductNotFoundException;
import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.CategoryRepository;
import com.martishyn.productapi.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Spy
    private ProductUpdateRequest productUpdateRequest;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private List<Product> products;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(ProductServiceTest.class);
    }

    @BeforeEach
    void contextLoads() {
        Category category = new Category("Test-category");
        Product product = new Product("Test-product1", BigDecimal.valueOf(100.00), category);
        Product product2 = new Product("Test-product2", BigDecimal.valueOf(100.00), category);
        products = new ArrayList<>();
        products.add(product);
        products.add(product2);
    }

    @Test
    void shouldReturnAllProductsWhenThereAreSome() {
        when(productRepository.findAll()).thenReturn(products);

        List<Product> allProducts = productService.findAllProducts();

        Assertions.assertEquals(products.size(), allProducts.size());
        Assertions.assertEquals(products.get(0).getId(), allProducts.get(0).getId());
        Assertions.assertEquals(products.get(0).getName(), allProducts.get(0).getName());
        Assertions.assertEquals(products.get(0).getPrice(), allProducts.get(0).getPrice());
        Assertions.assertEquals(products.get(0).getCategory(), allProducts.get(0).getCategory());
    }

    @Test
    void shouldThrowExceptionWhenProductsAreEmpty() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.findAllProducts());
    }

    @Test
    void shouldFindProductByIdWhenPresent() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(products.get(0)));

        Product foundProduct = productService.findProductById(1L);

        Assertions.assertEquals("Test-product1", foundProduct.getName());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.findProductById(null));
    }

    @Test
    void shouldCreateProduct() {
        Category category = new Category(1L, "newCategory");
        ProductCreateRequest newProduct = new ProductCreateRequest("newProduct", BigDecimal.valueOf(100.00));
        Product newProductCreated = new Product(1L, "newProduct", BigDecimal.valueOf(100.00), category);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(newProductCreated);

        Product product = productService.createProduct(newProduct, category.getId());

        Assertions.assertEquals(1L, product.getId());
        Assertions.assertEquals("newProduct", product.getName());
        Assertions.assertEquals("newCategory", product.getCategory().getName());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundDuringProductCreation() {
        Category category = new Category(1L, "newCategory");
        ProductCreateRequest newProduct = new ProductCreateRequest("newProduct", BigDecimal.valueOf(100.00));

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(newProduct, category.getId()));
    }

    @Test
    void shouldThrowExceptionWhenPassingNullProduct() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(null, 1L));
    }

    @Test
    void shouldThrowExceptionWhenPassingNullCategoryId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(new ProductCreateRequest(), null));
    }

    @Test
    void shouldFindProductByCategory() {
        Category category = new Category(1L, "newCategory");
        Product newProduct = new Product("newProduct", BigDecimal.valueOf(100.00), category);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(productRepository.findProductsByCategory(category)).thenReturn(List.of(newProduct));

        Assertions.assertEquals(1, productService.findProductsByCategory(category.getId()).size());
        Assertions.assertEquals("newProduct", productService.findProductsByCategory(category.getId()).get(0).getName());
        Assertions.assertEquals("newCategory", productService.findProductsByCategory(category.getId()).get(0).getCategory().getName());
    }

    @Test
    void shouldFindNotProductByCategoryWhenCategoryIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.findProductsByCategory(null));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundDuringProductUpdate() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(productUpdateRequest.getId()).thenReturn(1L);
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productUpdateRequest));
    }

    @Test
    void shouldUpdateProduct() {
        Category category = new Category(1L, "newCategory");
        ProductUpdateRequest newProduct = new ProductUpdateRequest(1L, "newProduct", BigDecimal.valueOf(100.00), 1L);
        Product oldProduct = new Product(1L,"oldProduct", BigDecimal.valueOf(150.00), category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(oldProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(oldProduct);

        Product product = productService.updateProduct(newProduct);

        Assertions.assertEquals("newProduct", product.getName());
        Assertions.assertEquals("newCategory", product.getCategory().getName());
        Assertions.assertEquals(BigDecimal.valueOf(100.00), oldProduct.getPrice());
    }

    @Test
    void shouldThrowExceptionWhenPassingNullAsIdDuringProductUpdate() {
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(new ProductUpdateRequest()));
    }

    @Test
    void shouldDeleteProductById() {
        doNothing().when(productRepository).deleteById(any(Long.class));
        productService.deleteProductById(any(Long.class));
    }

    @Test
    void shouldNotDeleteProductByIdWhenPassingNullAsId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.deleteProductById(null));
    }

    @Test
    void shouldFindProductByPriceBetween() {
        when(productRepository.findByPriceBetween(BigDecimal.valueOf(10), BigDecimal.valueOf(100))).thenReturn(products);

        List<Product> productByPriceBetween = productService.findProductByPriceBetween(BigDecimal.valueOf(10), BigDecimal.valueOf(100));

        Assertions.assertEquals(2, productByPriceBetween.size());
    }

    @Test
    void shouldThrowExceptionWhenFindProductByPriceBetweenWithNullArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.findProductByPriceBetween(null, null));
    }

    @Test
    void shouldFindProductByPriceLessThanAndCategory() {
        Category category = new Category(1L, "newCategory");
        when(productRepository.findProductByCategoryAndPriceLessThan(category, BigDecimal.valueOf(10))).thenReturn(products);

        List<Product> productByPriceBetween = productService.findProductByCategoryAndPrice(BigDecimal.valueOf(10), category);

        Assertions.assertEquals(2, productByPriceBetween.size());
    }

    @Test
    void shouldThrowExceptionWhenFindProductByPriceLessThanAndCategoryWithNullArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.findProductByCategoryAndPrice(null, null),
                "Passing null arguments to ProductService#findProductByCategoryAndPrice");
    }
}
