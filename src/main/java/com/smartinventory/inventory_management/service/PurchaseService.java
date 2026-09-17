package com.smartinventory.inventory_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.entity.Product;
import com.smartinventory.inventory_management.entity.Purchase;
import com.smartinventory.inventory_management.repository.ProductRepository;
import com.smartinventory.inventory_management.repository.PurchaseRepository;
import com.smartinventory.inventory_management.repository.SupplierRepository;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public PurchaseService(
            PurchaseRepository purchaseRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository) {

        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    // Get purchase history by supplier ID
    public List<Purchase> getPurchasesBySupplierId(Long supplierId) {
        return purchaseRepository.findBySupplierId(supplierId);
    }

    // Get supplier purchase summary
    public SupplierPurchaseSummary getSupplierPurchaseSummary(Long supplierId) {

        List<Purchase> purchases
                = purchaseRepository.findBySupplierId(supplierId);

        int totalPurchases = purchases.size();

        int totalQuantity = purchases.stream()
                .mapToInt(Purchase::getQuantity)
                .sum();

        double totalAmount = purchases.stream()
                .mapToDouble(purchase
                        -> purchase.getQuantity()
                * purchase.getPurchasePrice())
                .sum();

        totalAmount = round(totalAmount);

        return new SupplierPurchaseSummary(
                supplierId,
                totalPurchases,
                totalQuantity,
                totalAmount
        );
    }

    public Purchase createPurchase(Purchase purchase) {

        // Check supplier only if supplier ID is provided
        if (purchase.getSupplierId() != null) {

            supplierRepository.findById(
                    purchase.getSupplierId()
            ).orElseThrow(()
                    -> new RuntimeException("Supplier not found")
            );
        }

        // Check product
        Product product = productRepository.findById(
                purchase.getProductId()
        ).orElseThrow(()
                -> new RuntimeException("Product not found")
        );

        if (purchase.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        if (purchase.getPurchasePrice() < 0) {
            throw new IllegalArgumentException(
                    "Purchase price cannot be negative"
            );
        }

        // Existing stock and purchase price
        int existingStock = product.getStockQuantity();

        double existingPurchasePrice
                = product.getPurchasePrice();

        // Existing stock value
        double existingStockValue
                = existingStock * existingPurchasePrice;

        // New purchase value
        double newPurchaseValue
                = purchase.getQuantity()
                * purchase.getPurchasePrice();

        // Total stock
        int totalStock
                = existingStock + purchase.getQuantity();

        // Weighted average purchase price
        double averagePurchasePrice
                = (existingStockValue + newPurchaseValue)
                / totalStock;

        averagePurchasePrice
                = round(averagePurchasePrice);

        // Update product
        product.setPurchasePrice(
                averagePurchasePrice
        );

        product.setStockQuantity(totalStock);

        productRepository.save(product);

        // Set purchase date
        purchase.setDate(LocalDateTime.now());

        return purchaseRepository.save(purchase);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
