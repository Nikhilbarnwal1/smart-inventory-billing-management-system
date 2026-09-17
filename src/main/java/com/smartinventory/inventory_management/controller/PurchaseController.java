package com.smartinventory.inventory_management.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.inventory_management.entity.Purchase;
import com.smartinventory.inventory_management.service.PurchaseService;
import com.smartinventory.inventory_management.service.SupplierPurchaseSummary;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public Purchase createPurchase(@RequestBody Purchase purchase) {
        return purchaseService.createPurchase(purchase);
    }

    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    // Get purchase history by supplier ID
    @GetMapping("/supplier/{supplierId}")
    public List<Purchase> getPurchasesBySupplierId(
            @PathVariable Long supplierId) {

        return purchaseService.getPurchasesBySupplierId(supplierId);
    }

    // Get supplier purchase summary
    @GetMapping("/supplier/{supplierId}/summary")
    public SupplierPurchaseSummary getSupplierPurchaseSummary(
            @PathVariable Long supplierId) {

        return purchaseService.getSupplierPurchaseSummary(supplierId);
    }
}
