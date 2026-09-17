package com.smartinventory.inventory_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartinventory.inventory_management.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByProductId(Long productId);

    List<Purchase> findBySupplierId(Long supplierId);

}
