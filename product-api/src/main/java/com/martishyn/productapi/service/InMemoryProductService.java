package com.martishyn.productapi.service;

import com.martishyn.productapi.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryProductService implements ProductService {

    private List<Product> products;

    private AtomicLong productIdSequence = new AtomicLong(1);

    public InMemoryProductService(List<Product> products) {
        this.products = products;
    }

    @Override
    public List<Product> getAllProducts() {
        if (CollectionUtils.isEmpty(products)) {
            return List.of();
        }
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Override
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product newProduct = Product.builder()
                .id(productIdSequence.getAndIncrement())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
        products.add(newProduct);
        return newProduct;
    }

    @Override
    public Product updateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        products.stream()
                .filter(p -> p.getId().equals(product.getId()))
                .findFirst()
                .ifPresent(p -> updateEntireProduct(product, p));
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (products.isEmpty()) {
            throw new IllegalArgumentException("No products found");
        }
        Product productToRemove = getProductById(id);
        products.remove(productToRemove);
    }

    private void updateEntireProduct(Product productFromRequest, Product productToUpdate) {
        productToUpdate.setName(productFromRequest.getName());
        productToUpdate.setCategory(productFromRequest.getCategory());
        productToUpdate.setPrice(productFromRequest.getPrice());
    }
}
