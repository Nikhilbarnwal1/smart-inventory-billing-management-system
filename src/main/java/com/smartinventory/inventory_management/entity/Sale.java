package com.smartinventory.inventory_management.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@JsonPropertyOrder({
    "id",
    "productId",
    "customerId",
    "customerName",
    "gstNumber",
    "quantity",
    "sellingPrice",
    "date",
    "subtotal",
    "discountPercentage",
    "finalAmount",
    "cgstPercentage",
    "cgstAmount",
    "sgstPercentage",
    "sgstAmount",
    "grandTotal",
    "purchasePriceAtSale",
    "paymentMethod",
    "paidAmount",
    "pendingAmount"
})
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long customerId;

    private String customerName;

    // Optional GST Number
    private String gstNumber;

    private int quantity;

    private double sellingPrice;

    private LocalDateTime date;

    private double subtotal;

    private double discountPercentage;

    private double finalAmount;

    // GST fields
    private double cgstPercentage;
    private double sgstPercentage;

    private double cgstAmount;
    private double sgstAmount;

    private double grandTotal;

    // Purchase price at the time of sale
    private double purchasePriceAtSale;

    // Payment details
    private String paymentMethod;

    private double paidAmount;

    private double pendingAmount;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    // CGST
    public double getCgstPercentage() {
        return cgstPercentage;
    }

    public void setCgstPercentage(double cgstPercentage) {
        this.cgstPercentage = cgstPercentage;
    }

    public double getCgstAmount() {
        return cgstAmount;
    }

    public void setCgstAmount(double cgstAmount) {
        this.cgstAmount = cgstAmount;
    }

    // SGST
    public double getSgstPercentage() {
        return sgstPercentage;
    }

    public void setSgstPercentage(double sgstPercentage) {
        this.sgstPercentage = sgstPercentage;
    }

    public double getSgstAmount() {
        return sgstAmount;
    }

    public void setSgstAmount(double sgstAmount) {
        this.sgstAmount = sgstAmount;
    }

    // Grand Total
    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    // Purchase Price At Sale
    public double getPurchasePriceAtSale() {
        return purchasePriceAtSale;
    }

    public void setPurchasePriceAtSale(double purchasePriceAtSale) {
        this.purchasePriceAtSale = purchasePriceAtSale;
    }

    // Payment Method
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Paid Amount
    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    // Pending Amount
    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }
}
