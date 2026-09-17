package com.smartinventory.inventory_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.inventory_management.entity.CustomerPayment;
import com.smartinventory.inventory_management.service.CustomerPaymentService;

@RestController
@RequestMapping("/api/customer-payments")
public class CustomerPaymentController {

    private final CustomerPaymentService customerPaymentService;

    public CustomerPaymentController(
            CustomerPaymentService customerPaymentService) {

        this.customerPaymentService
                = customerPaymentService;
    }

    @PostMapping
    public CustomerPayment addPayment(
            @RequestBody CustomerPayment payment) {

        return customerPaymentService.addPayment(payment);
    }

    @GetMapping
    public List<CustomerPayment> getAllPayments() {
        return customerPaymentService.getAllPayments();
    }

    @GetMapping("/customer/{customerId}")
    public List<CustomerPayment> getCustomerPayments(
            @PathVariable Long customerId) {

        return customerPaymentService
                .getCustomerPayments(customerId);
    }
}
