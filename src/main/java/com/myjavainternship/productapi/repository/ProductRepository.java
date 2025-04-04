package com.myjavainternship.productapi.repository;

import com.myjavainternship.productapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Marks this interface as a Spring Data repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository provides standard CRUD methods like findAll(), findById(), save(), deleteById()
    // No need to write implementations for these basic operations.
    // You can add custom query methods here if needed (e.g., findByName(String name))
}