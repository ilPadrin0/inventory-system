package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
