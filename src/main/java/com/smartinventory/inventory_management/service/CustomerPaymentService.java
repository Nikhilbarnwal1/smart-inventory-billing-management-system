package com.smartinventory.inventory_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.entity.CustomerPayment;
import com.smartinventory.inventory_management.repository.CustomerPaymentRepository;
import com.smartinventory.inventory_management.repository.CustomerRepository;

@Service
public class CustomerPaymentService {

    private final CustomerPaymentRepository customerPaymentRepository;
    private final CustomerRepository customerRepository;

    public CustomerPaymentService(
            CustomerPaymentRepository customerPaymentRepository,
            CustomerRepository customerRepository) {

        this.customerPaymentRepository
                = customerPaymentRepository;

        this.customerRepository
                = customerRepository;
    }

    public CustomerPayment addPayment(
            CustomerPayment payment) {

        if (payment.getCustomerId() == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required"
            );
        }

        customerRepository.findById(
                payment.getCustomerId()
        ).orElseThrow(()
                -> new RuntimeException(
                        "Customer not found"
                )
        );

        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than 0"
            );
        }

        if (payment.getPaymentMethod() == null
                || payment.getPaymentMethod().isBlank()) {

            throw new IllegalArgumentException(
                    "Payment method is required"
            );
        }

        if (payment.getType() == null
                || payment.getType().isBlank()) {

            payment.setType("PAYMENT");
        }

        payment.setDate(LocalDateTime.now());

        return customerPaymentRepository.save(payment);
    }

    public List<CustomerPayment> getCustomerPayments(
            Long customerId) {

        customerRepository.findById(
                customerId
        ).orElseThrow(()
                -> new RuntimeException(
                        "Customer not found"
                )
        );

        return customerPaymentRepository
                .findByCustomerId(customerId);
    }

    public List<CustomerPayment> getAllPayments() {
        return customerPaymentRepository.findAll();
    }
}
