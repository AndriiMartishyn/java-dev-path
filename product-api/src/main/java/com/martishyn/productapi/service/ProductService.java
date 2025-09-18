package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.dto.ProductUpdateRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductCreateRequest product);

    ProductResponseDto updateProduct(ProductUpdateRequest product);

    void deleteProduct(Long id);

    List<ProductResponseDto> findProductsByCategory(String category);

    List<ProductResponseDto> findProductsByPriceRange(BigDecimal min, BigDecimal max);

    List<ProductResponseDto> findProductsWithSearch(String category, BigDecimal maxPrice);
}
