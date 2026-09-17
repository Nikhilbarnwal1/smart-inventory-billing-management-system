package com.smartinventory.inventory_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartinventory.inventory_management.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByStockQuantityLessThan(int quantity);

}
