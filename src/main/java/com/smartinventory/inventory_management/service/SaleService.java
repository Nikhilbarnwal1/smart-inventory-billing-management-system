package com.smartinventory.inventory_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.entity.CustomerPayment;
import com.smartinventory.inventory_management.entity.Product;
import com.smartinventory.inventory_management.entity.Sale;
import com.smartinventory.inventory_management.repository.CustomerPaymentRepository;
import com.smartinventory.inventory_management.repository.CustomerRepository;
import com.smartinventory.inventory_management.repository.ProductRepository;
import com.smartinventory.inventory_management.repository.SaleRepository;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CustomerPaymentRepository customerPaymentRepository;

    public SaleService(
            SaleRepository saleRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            CustomerPaymentRepository customerPaymentRepository) {

        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.customerPaymentRepository = customerPaymentRepository;
    }

    public Sale createSale(Sale sale) {

        Product product = productRepository.findById(
                sale.getProductId()
        ).orElseThrow(()
                -> new RuntimeException("Product not found")
        );

        if (sale.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        if (product.getStockQuantity() < sale.getQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock"
            );
        }

        if (sale.getSellingPrice() < 0) {
            throw new IllegalArgumentException(
                    "Selling price cannot be negative"
            );
        }

        if (sale.getDiscountPercentage() < 0) {
            throw new IllegalArgumentException(
                    "Discount percentage cannot be negative"
            );
        }

        if (sale.getCgstPercentage() < 0
                || sale.getSgstPercentage() < 0) {

            throw new IllegalArgumentException(
                    "GST percentage cannot be negative"
            );
        }

        if (sale.getPaidAmount() < 0) {
            throw new IllegalArgumentException(
                    "Paid amount cannot be negative"
            );
        }

        if (sale.getCustomerId() != null) {

            customerRepository.findById(
                    sale.getCustomerId()
            ).orElseThrow(()
                    -> new RuntimeException("Customer not found")
            );
        }

        sale.setDate(LocalDateTime.now());

        sale.setPurchasePriceAtSale(
                product.getPurchasePrice()
        );

        double subtotal
                = sale.getQuantity()
                * sale.getSellingPrice();

        subtotal = round(subtotal);

        sale.setSubtotal(subtotal);

        double discountAmount
                = subtotal
                * sale.getDiscountPercentage()
                / 100.0;

        discountAmount = round(discountAmount);

        double finalAmount
                = subtotal - discountAmount;

        finalAmount = round(finalAmount);

        sale.setFinalAmount(finalAmount);

        double cgstAmount
                = finalAmount
                * sale.getCgstPercentage()
                / 100.0;

        cgstAmount = round(cgstAmount);

        sale.setCgstAmount(cgstAmount);

        double sgstAmount
                = finalAmount
                * sale.getSgstPercentage()
                / 100.0;

        sgstAmount = round(sgstAmount);

        sale.setSgstAmount(sgstAmount);

        double grandTotal
                = finalAmount
                + cgstAmount
                + sgstAmount;

        grandTotal = round(grandTotal);

        sale.setGrandTotal(grandTotal);

        double pendingAmount
                = Math.max(
                        0,
                        grandTotal - sale.getPaidAmount()
                );

        pendingAmount = round(pendingAmount);

        sale.setPendingAmount(pendingAmount);

        product.setStockQuantity(
                product.getStockQuantity()
                - sale.getQuantity()
        );

        productRepository.save(product);

        Sale savedSale = saleRepository.save(sale);

        /*
         * Automatically create a customer payment ledger entry
         * when a customer pays an amount during the sale.
         */
        if (sale.getCustomerId() != null
                && sale.getPaidAmount() > 0) {

            CustomerPayment payment
                    = new CustomerPayment();

            payment.setCustomerId(
                    sale.getCustomerId()
            );

            payment.setAmount(
                    sale.getPaidAmount()
            );

            payment.setPaymentMethod(
                    sale.getPaymentMethod()
            );

            payment.setType("SALE_PAYMENT");

            payment.setDate(
                    LocalDateTime.now()
            );

            payment.setNote(
                    "Payment for Sale #" + savedSale.getId()
            );

            customerPaymentRepository.save(payment);
        }

        return savedSale;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public List<Sale> getSalesByCustomerId(Long customerId) {

        customerRepository.findById(
                customerId
        ).orElseThrow(()
                -> new RuntimeException("Customer not found")
        );

        return saleRepository.findByCustomerId(customerId);
    }

    public CustomerSalesSummary getCustomerSalesSummary(
            Long customerId) {

        customerRepository.findById(
                customerId
        ).orElseThrow(()
                -> new RuntimeException("Customer not found")
        );

        List<Sale> sales
                = saleRepository.findByCustomerId(customerId);

        int totalOrders = sales.size();

        int totalQuantity = sales.stream()
                .mapToInt(Sale::getQuantity)
                .sum();

        double totalAmount = sales.stream()
                .mapToDouble(Sale::getGrandTotal)
                .sum();

        totalAmount = round(totalAmount);

        return new CustomerSalesSummary(
                customerId,
                totalOrders,
                totalQuantity,
                totalAmount
        );
    }

    public Sale getSaleById(Long id) {

        return saleRepository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException(
                        "Sale not found"
                )
                );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
