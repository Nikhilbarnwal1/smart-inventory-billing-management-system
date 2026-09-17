package com.smartinventory.inventory_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartinventory.inventory_management.entity.Product;
import com.smartinventory.inventory_management.entity.StockHistory;
import com.smartinventory.inventory_management.exception.InsufficientStockException;
import com.smartinventory.inventory_management.repository.ProductRepository;
import com.smartinventory.inventory_management.repository.StockHistoryRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StockHistoryRepository stockHistoryRepository;

    public ProductService(ProductRepository productRepository,
            StockHistoryRepository stockHistoryRepository) {

        this.productRepository = productRepository;
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    public List<Product> getLowStockProducts(int limit) {
        return productRepository.findByStockQuantityLessThan(limit);
    }

    // Get overall inventory summary
    public InventorySummary getInventorySummary() {

        List<Product> products = productRepository.findAll();

        int totalProducts = products.size();

        int totalStockQuantity = products.stream()
                .mapToInt(Product::getStockQuantity)
                .sum();

        double totalInventoryValue = products.stream()
                .mapToDouble(product ->
                        product.getStockQuantity()
                        * product.getPurchasePrice())
                .sum();

        int lowStockProducts = (int) products.stream()
                .filter(product -> product.getStockQuantity() < 10)
                .count();

        totalInventoryValue = round(totalInventoryValue);

        return new InventorySummary(
                totalProducts,
                totalStockQuantity,
                totalInventoryValue,
                lowStockProducts
        );
    }

    public Product addStock(Long id, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Stock quantity must be greater than 0");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(
                product.getStockQuantity() + quantity);

        StockHistory history = new StockHistory();
        history.setProductId(id);
        history.setType("ADD");
        history.setQuantity(quantity);
        history.setDate(LocalDateTime.now());

        stockHistoryRepository.save(history);

        return productRepository.save(product);
    }

    public Product reduceStock(Long id, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Stock quantity must be greater than 0");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock");
        }

        product.setStockQuantity(
                product.getStockQuantity() - quantity);

        StockHistory history = new StockHistory();
        history.setProductId(id);
        history.setType("REDUCE");
        history.setQuantity(quantity);
        history.setDate(LocalDateTime.now());

        stockHistoryRepository.save(history);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}