package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(object -> modelMapper.map(object, ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return modelMapper.map(productRepository.findById(id), ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto createProduct(ProductCreateRequest product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product newProduct = Product.builder()
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
        return modelMapper.map(productRepository.save(newProduct), ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto updateProduct(ProductUpdateRequest product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        final Product updatedProduct = productRepository.save(modelMapper.map(product, Product.class));
        return modelMapper.map(updatedProduct, ProductResponseDto.class);
    }

    @Override
    public void deleteProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        checkIfProductsExist();
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDto> findProductsByCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        checkIfProductsExist();
        List<Product> foundProducts = productRepository.findByCategory(category);
        return foundProducts.stream()
                .map(object -> modelMapper.map(object, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<ProductResponseDto> findProductsByPriceRange(BigDecimal min, BigDecimal max) {
        checkIfProductsExist();
        return productRepository.findByPriceBetween(min, max)
                .stream()
                .map(object -> modelMapper.map(object, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<ProductResponseDto> findProductsWithSearch(String category, BigDecimal maxPrice) {
        checkIfProductsExist();
        return productRepository.findByCategoryAndPriceLessThan(category, maxPrice);
    }

    private void checkIfProductsExist() {
        if (productRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException("No products found");
        }
    }
}
