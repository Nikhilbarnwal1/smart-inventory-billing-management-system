package com.smartinventory.inventory_management.service;

public class CustomerBalanceSummary {

    private Long customerId;

    private double totalBills;

    private double totalPayments;

    private double outstandingBalance;

    private double advanceAmount;

    public CustomerBalanceSummary(
            Long customerId,
            double totalBills,
            double totalPayments,
            double outstandingBalance,
            double advanceAmount) {

        this.customerId = customerId;
        this.totalBills = totalBills;
        this.totalPayments = totalPayments;
        this.outstandingBalance = outstandingBalance;
        this.advanceAmount = advanceAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public double getTotalBills() {
        return totalBills;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public double getAdvanceAmount() {
        return advanceAmount;
    }
}
