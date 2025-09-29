package com.martishyn.productapi.repository;

import com.martishyn.productapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o JOIN FETCH o.products WHERE o.id = :orderId")
    Optional<Order> findWithProducts(Long orderId);
}
