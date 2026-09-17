package com.smartinventory.inventory_management.service;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.entity.Sale;
import com.smartinventory.inventory_management.repository.SaleRepository;

@Service
public class InvoiceService {

    private final SaleRepository saleRepository;

    public InvoiceService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Sale getSaleForInvoice(Long saleId) {

        return saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }
}
