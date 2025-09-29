package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.exceptions.CategoryNotFoundException;
import com.martishyn.productapi.exceptions.ProductNotFoundException;
import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.CategoryRepository;
import com.martishyn.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public Product createProduct(ProductCreateRequest product, Long categoryId) {
        if (product == null || categoryId == null) {
            throw new IllegalArgumentException("Passing null arguments to ProductService#createProduct");
        }
        Category foundCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("Category id " + categoryId + " not found"));
        Product productToCreate = createProductFromDto(product);
        productToCreate.setCategory(foundCategory);
        return productRepository.save(productToCreate);
    }

    public Product findProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        return productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product id " + id + " not found"));
    }

    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found");
        }
        return products;
    }

    public List<Product> findProductsByCategory(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId is null");
        }
        Category foundCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("Category id " + categoryId + " not found"));
        return productRepository.findProductsByCategory(foundCategory);
    }

    public Product updateProduct(ProductUpdateRequest product) {
        if (product == null) {
            throw new IllegalArgumentException("Passing null arguments to ProductService#updateProduct");
        }
        Product foundProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new ProductNotFoundException("Product id " + product.getId() + " not found"));
        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category id " + product.getCategoryId() + " not found"));
        foundProduct.setName(product.getName());
        foundProduct.setCategory(category);
        foundProduct.setPrice(product.getPrice());
        return productRepository.save(foundProduct);
    }

    public void deleteProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        productRepository.deleteById(id);
    }

    public List<Product> findProductByPriceBetween(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Passing null arguments to ProductService#findProductByPriceBetween");
        }
        return productRepository.findByPriceBetween(min, max);
    }

    public List<Product> findProductByCategoryAndPrice(BigDecimal minPrice, Category category) {
        if (minPrice == null || category == null) {
            throw new IllegalArgumentException("Passing null arguments to ProductService#findProductByCategoryAndPrice");
        }
        return productRepository.findProductByCategoryAndPriceLessThan(category, minPrice);
    }

    private Product createProductFromDto(ProductCreateRequest product) {
        return new Product(product.getName(), product.getPrice());
    }

    public Set<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductByIds(productIds);
    }
}
