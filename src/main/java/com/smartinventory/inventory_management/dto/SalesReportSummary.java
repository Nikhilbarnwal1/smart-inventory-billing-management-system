package com.smartinventory.inventory_management.dto;

public class SalesReportSummary {

    private int totalSales;
    private double totalSubtotal;
    private double totalDiscount;
    private double totalTaxableAmount;
    private double totalCgst;
    private double totalSgst;
    private double totalGrandAmount;
    private int totalQuantitySold;
    private double totalPurchaseCost;
    private double totalProfit;

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalSubtotal() {
        return totalSubtotal;
    }

    public void setTotalSubtotal(double totalSubtotal) {
        this.totalSubtotal = totalSubtotal;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public void setTotalTaxableAmount(double totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }

    public double getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(double totalCgst) {
        this.totalCgst = totalCgst;
    }

    public double getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(double totalSgst) {
        this.totalSgst = totalSgst;
    }

    public double getTotalGrandAmount() {
        return totalGrandAmount;
    }

    public void setTotalGrandAmount(double totalGrandAmount) {
        this.totalGrandAmount = totalGrandAmount;
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(int totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }

    public double getTotalPurchaseCost() {
        return totalPurchaseCost;
    }

    public void setTotalPurchaseCost(double totalPurchaseCost) {
        this.totalPurchaseCost = totalPurchaseCost;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }
}
