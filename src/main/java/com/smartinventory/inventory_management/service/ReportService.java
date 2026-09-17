package com.smartinventory.inventory_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.dto.SalesReportSummary;
import com.smartinventory.inventory_management.entity.Sale;
import com.smartinventory.inventory_management.repository.SaleRepository;

@Service
public class ReportService {

    private final SaleRepository saleRepository;

    public ReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<Sale> getSalesBetweenDates(
            LocalDateTime from,
            LocalDateTime to) {

        return saleRepository.findByDateBetween(from, to);
    }

    public SalesReportSummary getSalesSummary(
            LocalDateTime from,
            LocalDateTime to) {

        List<Sale> sales
                = saleRepository.findByDateBetween(from, to);

        SalesReportSummary summary
                = new SalesReportSummary();

        summary.setTotalSales(sales.size());

        int totalQuantitySold = 0;
        double totalSubtotal = 0;
        double totalDiscount = 0;
        double totalTaxableAmount = 0;
        double totalCgst = 0;
        double totalSgst = 0;
        double totalGrandAmount = 0;
        double totalPurchaseCost = 0;
        double totalProfit = 0;

        for (Sale sale : sales) {

            totalQuantitySold += sale.getQuantity();

            totalSubtotal += sale.getSubtotal();

            double discountAmount
                    = sale.getSubtotal()
                    * sale.getDiscountPercentage()
                    / 100;

            totalDiscount += discountAmount;

            totalTaxableAmount += sale.getFinalAmount();
            totalCgst += sale.getCgstAmount();
            totalSgst += sale.getSgstAmount();
            totalGrandAmount += sale.getGrandTotal();

            // Use purchase price stored at the time of sale
            double purchaseCost
                    = sale.getQuantity()
                    * sale.getPurchasePriceAtSale();

            totalPurchaseCost += purchaseCost;

            // Profit before GST
            double profit
                    = sale.getFinalAmount()
                    - purchaseCost;

            totalProfit += profit;
        }

        summary.setTotalQuantitySold(totalQuantitySold);
        summary.setTotalSubtotal(round(totalSubtotal));
        summary.setTotalDiscount(round(totalDiscount));
        summary.setTotalTaxableAmount(round(totalTaxableAmount));
        summary.setTotalCgst(round(totalCgst));
        summary.setTotalSgst(round(totalSgst));
        summary.setTotalGrandAmount(round(totalGrandAmount));
        summary.setTotalPurchaseCost(round(totalPurchaseCost));
        summary.setTotalProfit(round(totalProfit));

        return summary;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
