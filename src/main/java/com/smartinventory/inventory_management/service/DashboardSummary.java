package com.smartinventory.inventory_management.service;

public class DashboardSummary {

    private long totalProducts;
    private long totalStockQuantity;
    private double totalInventoryValue;
    private long lowStockProducts;

    private double totalSales;
    private double totalPurchases;
    private double totalProfit;

    private long totalCustomers;
    private long totalSuppliers;

    public DashboardSummary() {
    }

    public DashboardSummary(long totalProducts,
            long totalStockQuantity,
            double totalInventoryValue,
            long lowStockProducts,
            double totalSales,
            double totalPurchases,
            double totalProfit,
            long totalCustomers,
            long totalSuppliers) {

        this.totalProducts = totalProducts;
        this.totalStockQuantity = totalStockQuantity;
        this.totalInventoryValue = totalInventoryValue;
        this.lowStockProducts = lowStockProducts;
        this.totalSales = totalSales;
        this.totalPurchases = totalPurchases;
        this.totalProfit = totalProfit;
        this.totalCustomers = totalCustomers;
        this.totalSuppliers = totalSuppliers;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalStockQuantity() {
        return totalStockQuantity;
    }

    public void setTotalStockQuantity(long totalStockQuantity) {
        this.totalStockQuantity = totalStockQuantity;
    }

    public double getTotalInventoryValue() {
        return totalInventoryValue;
    }

    public void setTotalInventoryValue(double totalInventoryValue) {
        this.totalInventoryValue = totalInventoryValue;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(double totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(long totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }
}
