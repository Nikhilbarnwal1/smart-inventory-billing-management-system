package com.smartinventory.inventory_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.entity.CustomerPayment;
import com.smartinventory.inventory_management.repository.CustomerPaymentRepository;
import com.smartinventory.inventory_management.repository.CustomerRepository;
import com.smartinventory.inventory_management.repository.SaleRepository;

@Service
public class CustomerBalanceService {

    private final SaleRepository saleRepository;
    private final CustomerPaymentRepository customerPaymentRepository;
    private final CustomerRepository customerRepository;

    public CustomerBalanceService(
            SaleRepository saleRepository,
            CustomerPaymentRepository customerPaymentRepository,
            CustomerRepository customerRepository) {

        this.saleRepository = saleRepository;
        this.customerPaymentRepository = customerPaymentRepository;
        this.customerRepository = customerRepository;
    }

    public CustomerBalanceSummary getCustomerBalance(
            Long customerId) {

        customerRepository.findById(
                customerId
        ).orElseThrow(()
                -> new RuntimeException(
                        "Customer not found"
                )
        );

        double totalBills = saleRepository
                .findByCustomerId(customerId)
                .stream()
                .mapToDouble(sale -> sale.getGrandTotal())
                .sum();

        List<CustomerPayment> payments
                = customerPaymentRepository
                        .findByCustomerId(customerId);

        double totalPayments = payments.stream()
                .mapToDouble(CustomerPayment::getAmount)
                .sum();

        totalBills = round(totalBills);
        totalPayments = round(totalPayments);

        double balance
                = totalBills - totalPayments;

        double outstandingBalance
                = Math.max(0, balance);

        double advanceAmount
                = Math.max(0, -balance);

        outstandingBalance
                = round(outstandingBalance);

        advanceAmount
                = round(advanceAmount);

        return new CustomerBalanceSummary(
                customerId,
                totalBills,
                totalPayments,
                outstandingBalance,
                advanceAmount
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
