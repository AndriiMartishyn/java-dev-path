package com.martishyn.productapi.service;

import com.martishyn.productapi.dto.ProductCreateRequest;
import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.dto.ProductUpdateRequest;
import com.martishyn.productapi.model.Product;
import com.martishyn.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private ProductRepository productRepository;

    private ModelMapper modelMapper;

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
        if (productRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException("No products found");
        }
        productRepository.deleteById(id);
    }
}
