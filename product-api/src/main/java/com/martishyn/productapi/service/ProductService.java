package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.dto.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductCreateRequest product);

    ProductResponseDto updateProduct(ProductUpdateRequest product);

    void deleteProduct(Long id);
}
