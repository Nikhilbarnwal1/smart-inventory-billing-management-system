package com.smartinventory.inventory_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartinventory.inventory_management.entity.CustomerPayment;

public interface CustomerPaymentRepository
        extends JpaRepository<CustomerPayment, Long> {

    List<CustomerPayment> findByCustomerId(Long customerId);
}
