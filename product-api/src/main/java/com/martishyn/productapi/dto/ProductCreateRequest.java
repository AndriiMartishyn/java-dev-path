package com.martishyn.productapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ProductCreateRequest {

    @Positive
    private Long id;

    @NotBlank
    private String name;

    @Min(0)
    private BigDecimal price;

    @NotBlank
    private String category;

    public ProductCreateRequest(String name, BigDecimal price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
