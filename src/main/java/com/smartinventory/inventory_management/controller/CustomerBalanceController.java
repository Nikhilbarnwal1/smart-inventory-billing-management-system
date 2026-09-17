package com.smartinventory.inventory_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.inventory_management.service.CustomerBalanceService;
import com.smartinventory.inventory_management.service.CustomerBalanceSummary;

@RestController
@RequestMapping("/api/customer-balance")
public class CustomerBalanceController {

    private final CustomerBalanceService customerBalanceService;

    public CustomerBalanceController(
            CustomerBalanceService customerBalanceService) {

        this.customerBalanceService
                = customerBalanceService;
    }

    @GetMapping("/customer/{customerId}")
    public CustomerBalanceSummary getCustomerBalance(
            @PathVariable Long customerId) {

        return customerBalanceService
                .getCustomerBalance(customerId);
    }
}
