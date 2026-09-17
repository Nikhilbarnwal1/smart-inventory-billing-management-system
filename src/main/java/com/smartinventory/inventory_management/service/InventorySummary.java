package com.smartinventory.inventory_management.service;

public class InventorySummary {

    private int totalProducts;
    private int totalStockQuantity;
    private double totalInventoryValue;
    private int lowStockProducts;

    public InventorySummary(
            int totalProducts,
            int totalStockQuantity,
            double totalInventoryValue,
            int lowStockProducts) {

        this.totalProducts = totalProducts;
        this.totalStockQuantity = totalStockQuantity;
        this.totalInventoryValue = totalInventoryValue;
        this.lowStockProducts = lowStockProducts;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public int getTotalStockQuantity() {
        return totalStockQuantity;
    }

    public double getTotalInventoryValue() {
        return totalInventoryValue;
    }

    public int getLowStockProducts() {
        return lowStockProducts;
    }
}