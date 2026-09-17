package com.smartinventory.inventory_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.inventory_management.entity.StockHistory;
import com.smartinventory.inventory_management.repository.StockHistoryRepository;

@RestController
@RequestMapping("/api/stock-history")
public class StockHistoryController {

    private final StockHistoryRepository stockHistoryRepository;

    public StockHistoryController(StockHistoryRepository stockHistoryRepository) {
        this.stockHistoryRepository = stockHistoryRepository;
    }

    @GetMapping("/{productId}")
    public List<StockHistory> getStockHistory(@PathVariable Long productId) {
        return stockHistoryRepository.findByProductId(productId);
    }
}
