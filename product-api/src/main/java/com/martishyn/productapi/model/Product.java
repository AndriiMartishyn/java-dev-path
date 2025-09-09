package com.martishyn.productapi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    Long id;

    String name;

    BigDecimal price;

    String category;
}
