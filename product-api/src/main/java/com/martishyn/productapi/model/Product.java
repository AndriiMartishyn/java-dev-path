package com.martishyn.productapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    private Long id;

    private String name;

    private BigDecimal price;

    private String category;

    public Product(String name, BigDecimal price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
