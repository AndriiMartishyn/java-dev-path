package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.model.Product;
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
import org.modelmapper.ModelMapper;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService = new DefaultProductService(productRepository);

    private List<Product> products;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(ProductServiceTest.class);
    }

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
    }

    @Test
    void shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponseDto> allProducts = productService.getAllProducts();

        Assertions.assertEquals(products.size(), allProducts.size());
        Assertions.assertEquals(products.get(0).getId(), allProducts.get(0).getId());
        Assertions.assertEquals(products.get(0).getName(), allProducts.get(0).getName());
        Assertions.assertEquals(products.get(0).getPrice(), allProducts.get(0).getPrice());
        Assertions.assertEquals(products.get(0).getCategory(), allProducts.get(0).getCategory());
    }

    @Test
    void shouldReturnProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(products.get(0)));
        ProductResponseDto product = productService.getProductById(products.get(0).getId());

        Assertions.assertEquals(1L, product.getId());
    }

    @Test
    void shouldCreateProductAndGenerateId() {
        ProductCreateRequest newProduct = new ProductCreateRequest("newProduct", BigDecimal.valueOf(100.00), "newCategory");

        Product createdProduct = Product.builder()
                .name(newProduct.getName())
                .category(newProduct.getCategory())
                .price(newProduct.getPrice())
                .build();
        when(productRepository.save(any())).thenReturn(createdProduct);

        ProductResponseDto product = productService.createProduct(newProduct);

        Assertions.assertEquals("newProduct", product.getName());
        Assertions.assertEquals("newCategory", product.getCategory());

    }

    @Test
    void shouldUpdateProduct() {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "product-updated", BigDecimal.valueOf(12.00), "category-updated");
        Product product = new Product(1L, "product-updated", BigDecimal.valueOf(12.00), "category-updated");

        when(productRepository.save(any())).thenReturn(product);

        ProductResponseDto productResponseDto = productService.updateProduct(productUpdateRequest);

        Assertions.assertEquals("product-updated", productResponseDto.getName());
        Assertions.assertEquals(BigDecimal.valueOf(12.00), productResponseDto.getPrice());
        Assertions.assertEquals("category-updated", productResponseDto.getCategory());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findAll()).thenReturn(List.of());

        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(1L));
    }


    @Test
    void shouldThrowExceptionWhenPassingNullAsId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(null));
    }

    @Test
    void shouldDeleteProduct() {
        when(productRepository.findAll()).thenReturn(products);
        doNothing().when(productRepository).deleteById(1L);
        productService.deleteProduct(1L);
    }
}
