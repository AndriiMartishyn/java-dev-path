package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductRequestDto;
import com.martishyn.productapi.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(ProductRequestDto product);

    Product updateProduct(ProductRequestDto product);

    void deleteProduct(Long id);
}
