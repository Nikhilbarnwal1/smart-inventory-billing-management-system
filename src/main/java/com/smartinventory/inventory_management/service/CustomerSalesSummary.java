package com.smartinventory.inventory_management.service;

public class CustomerSalesSummary {

    private Long customerId;
    private int totalOrders;
    private int totalQuantity;
    private double totalAmount;

    public CustomerSalesSummary(
            Long customerId,
            int totalOrders,
            int totalQuantity,
            double totalAmount) {

        this.customerId = customerId;
        this.totalOrders = totalOrders;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
