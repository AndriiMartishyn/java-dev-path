package com.martishyn.productapi.repository;

import com.martishyn.productapi.model.Category;
import com.martishyn.productapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
