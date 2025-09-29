package com.martishyn.productapi.repository;

import com.martishyn.productapi.model.Customer;
import com.martishyn.productapi.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    @Query("SELECT p.order.customer, SUM(p.amount) as total FROM Payment p GROUP BY p.order.customer ORDER BY total DESC")
    List<Object[]> findCustomerWithHighestPayments();

}
