package com.martishyn.productapi.repository;

import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    @Query("SELECT new com.martishyn.productapi.dto.ProductResponseDto(p.id, p.name, p.price, p.category)" +
            " FROM Product p WHERE p.category LIKE :category AND p.price < :maxPrice ORDER BY p.id")
    List<ProductResponseDto> findByCategoryAndPriceLessThan(@Param("category") String category,
                                                            @Param("maxPrice") BigDecimal maxPrice);

}
