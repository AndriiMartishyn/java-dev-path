package com.martishyn.productapi.dto;

import java.math.BigDecimal;

public record ProductRequestDto(String name, BigDecimal price, String category) {
}
