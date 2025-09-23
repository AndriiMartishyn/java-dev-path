package com.martishyn.productapi.controller;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> allProducts = productService.findAllProducts();
        if (allProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ProductResponseDto> productResponses = allProducts.stream()
                .map(obj -> modelMapper.map(obj, ProductResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        Product product = productService.findProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(modelMapper.map(product, ProductResponseDto.class));
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductCreateRequest product, @PathVariable Long categoryId) {
        Product createdProduct = productService.createProduct(product, categoryId);
        URI responseUri = UriComponentsBuilder.fromPath("/api/v1/products/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();
        return ResponseEntity.created(responseUri).body(createdProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody @Valid ProductUpdateRequest product) {
        Product updatedProduct = productService.updateProduct(product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(modelMapper.map(updatedProduct, ProductResponseDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> productsByCategory = productService.findProductsByCategory(categoryId);
        if (productsByCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ProductResponseDto> mappedProducts = productsByCategory.stream()
                .map(obj -> modelMapper.map(obj, ProductResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(mappedProducts);
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        List<Product> productByPriceBetween = productService.findProductByPriceBetween(min, max);
        if (productByPriceBetween.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ProductResponseDto> mappedProducts = productByPriceBetween.stream()
                .map(obj -> modelMapper.map(obj, ProductResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(mappedProducts);
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithSearchByPriceAndCategory(
            @RequestParam BigDecimal maxPrice,
            @RequestBody Category category) {
        List<Product> productByCategoryAndPrice = productService.findProductByCategoryAndPrice(maxPrice, category);
        if (productByCategoryAndPrice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ProductResponseDto> mappedProducts = productByCategoryAndPrice.stream()
                .map(obj -> modelMapper.map(obj, ProductResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(mappedProducts);
    }
}
