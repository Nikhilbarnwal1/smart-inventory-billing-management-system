package com.smartinventory.inventory_management.service;

public class SupplierPurchaseSummary {

    private Long supplierId;
    private int totalPurchases;
    private int totalQuantity;
    private double totalAmount;

    public SupplierPurchaseSummary(
            Long supplierId,
            int totalPurchases,
            int totalQuantity,
            double totalAmount) {

        this.supplierId = supplierId;
        this.totalPurchases = totalPurchases;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
