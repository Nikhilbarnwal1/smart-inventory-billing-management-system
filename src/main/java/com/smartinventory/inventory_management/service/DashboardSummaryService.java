package com.smartinventory.inventory_management.service;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.repository.CustomerRepository;
import com.smartinventory.inventory_management.repository.ProductRepository;
import com.smartinventory.inventory_management.repository.PurchaseRepository;
import com.smartinventory.inventory_management.repository.SaleRepository;
import com.smartinventory.inventory_management.repository.SupplierRepository;

@Service
public class DashboardSummaryService {

    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

    public DashboardSummaryService(ProductRepository productRepository,
            PurchaseRepository purchaseRepository,
            SaleRepository saleRepository,
            CustomerRepository customerRepository,
            SupplierRepository supplierRepository) {

        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
    }

    public DashboardSummary getDashboardSummary() {

        // Total products
        long totalProducts = productRepository.count();

        // Total stock quantity
        long totalStockQuantity = productRepository.findAll()
                .stream()
                .mapToLong(product -> product.getStockQuantity())
                .sum();

        // Total inventory value
        double totalInventoryValue = productRepository.findAll()
                .stream()
                .mapToDouble(product
                        -> product.getStockQuantity()
                * product.getPurchasePrice())
                .sum();

        // Low stock products
        long lowStockProducts = productRepository.findAll()
                .stream()
                .filter(product -> product.getStockQuantity() < 10)
                .count();

        // Total sales
        double totalSales = saleRepository.findAll()
                .stream()
                .mapToDouble(sale -> sale.getGrandTotal())
                .sum();

        // Total purchases
        double totalPurchases = purchaseRepository.findAll()
                .stream()
                .mapToDouble(purchase
                        -> purchase.getQuantity()
                * purchase.getPurchasePrice())
                .sum();

        // Total profit
        double totalProfit = saleRepository.findAll()
                .stream()
                .mapToDouble(sale
                        -> sale.getGrandTotal()
                - (sale.getQuantity()
                * sale.getPurchasePriceAtSale()))
                .sum();

        // Total customers
        long totalCustomers = customerRepository.count();

        // Total suppliers
        long totalSuppliers = supplierRepository.count();

        return new DashboardSummary(
                totalProducts,
                totalStockQuantity,
                round(totalInventoryValue),
                lowStockProducts,
                round(totalSales),
                round(totalPurchases),
                round(totalProfit),
                totalCustomers,
                totalSuppliers
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
