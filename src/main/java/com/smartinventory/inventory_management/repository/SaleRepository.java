package com.smartinventory.inventory_management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartinventory.inventory_management.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByDateBetween(
            LocalDateTime from,
            LocalDateTime to
    );

    List<Sale> findByCustomerId(Long customerId);

}
