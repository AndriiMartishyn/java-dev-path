package com.martishyn.productapi.repository;

import com.martishyn.productapi.dto.ProductResponseDto;
import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Product;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

//    List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findByPriceBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    List<Product> findProductsByCategory(Category foundCategory);

    @Query("SELECT p FROM Product p where p.category=:category AND p.price < :minPrice")
    List<Product> findProductByCategoryAndPriceLessThan(@Param("category") Category category,
                                                        @Param("minPrice") BigDecimal minPrice);

    @Query("SELECT p FROM Product p where p.id IN (:ids)")
    Set<Product> findProductByIds(@Param("ids") List<Long> productIds);

//    @Query("SELECT new com.martishyn.productapi.dto.ProductResponseDto(p.id, p.name, p.price, p.category)" +
//            " FROM Product p WHERE p.category LIKE :category AND p.price < :maxPrice ORDER BY p.id")
//    List<ProductResponseDto> findByCategoryAndPriceLessThan(@Param("category") String category,
//                                                            @Param("maxPrice") BigDecimal maxPrice);

}
