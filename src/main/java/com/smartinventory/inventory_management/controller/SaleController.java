package com.smartinventory.inventory_management.controller;

import com.smartinventory.inventory_management.entity.Sale;
import com.smartinventory.inventory_management.service.CustomerSalesSummary;
import com.smartinventory.inventory_management.service.InvoicePdfService;
import com.smartinventory.inventory_management.service.InvoiceService;
import com.smartinventory.inventory_management.service.SaleService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final InvoiceService invoiceService;
    private final InvoicePdfService invoicePdfService;

    public SaleController(
            SaleService saleService,
            InvoiceService invoiceService,
            InvoicePdfService invoicePdfService) {

        this.saleService = saleService;
        this.invoiceService = invoiceService;
        this.invoicePdfService = invoicePdfService;
    }

    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleService.createSale(sale);
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    // Get sales history by customer ID
    @GetMapping("/customer/{customerId}")
    public List<Sale> getSalesByCustomerId(
            @PathVariable Long customerId) {

        return saleService.getSalesByCustomerId(customerId);
    }

    // Get customer spending summary
    @GetMapping("/customer/{customerId}/summary")
    public CustomerSalesSummary getCustomerSalesSummary(
            @PathVariable Long customerId) {

        return saleService.getCustomerSalesSummary(customerId);
    }

    @GetMapping("/{saleId}/invoice")
    public Sale getInvoiceData(@PathVariable Long saleId) {
        return invoiceService.getSaleForInvoice(saleId);
    }

    @GetMapping("/{saleId}/invoice/pdf")
    public ResponseEntity<byte[]> generateInvoicePdf(
            @PathVariable Long saleId) {

        byte[] pdf = invoicePdfService.generateInvoicePdf(saleId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice-" + saleId + ".pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
