package com.smartinventory.inventory_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartinventory.inventory_management.entity.StockHistory;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {

    List<StockHistory> findByProductId(Long productId);

}
