package com.martishyn.productapi.controller;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductResponseDto> allProducts = productService.getAllProducts();
        if (allProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductCreateRequest product) {
        ProductResponseDto createdProduct = productService.createProduct(product);
        URI responseUri = UriComponentsBuilder.fromPath("/api/v1/products/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();
        return ResponseEntity.created(responseUri).body(createdProduct);
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductUpdateRequest product) {
        ProductResponseDto updatedProduct = productService.updateProduct(product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.findProductsByCategory(category));
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<ProductResponseDto>> getByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return ResponseEntity.ok(productService.findProductsByPriceRange(min, max));
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<List<ProductResponseDto>> advancedSearch(
            @RequestParam String category,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.findProductsWithSearch(category, maxPrice));
    }
}
