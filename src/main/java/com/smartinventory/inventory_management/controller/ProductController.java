package com.smartinventory.inventory_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartinventory.inventory_management.entity.Product;
import com.smartinventory.inventory_management.service.InventorySummary;
import com.smartinventory.inventory_management.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get overall inventory summary
    @GetMapping("/summary")
    public InventorySummary getInventorySummary() {
        return productService.getInventorySummary();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name);
    }

    @PutMapping("/{id}/add-stock")
    public Product addStock(
            @PathVariable Long id,
            @RequestParam int quantity) {

        return productService.addStock(id, quantity);
    }

    @PutMapping("/{id}/reduce-stock")
    public Product reduceStock(
            @PathVariable Long id,
            @RequestParam int quantity) {

        return productService.reduceStock(id, quantity);
    }

    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {

        product.setId(id);
        return productService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

    @GetMapping("/category/{category}")
    public List<Product> searchByCategory(
            @PathVariable String category) {

        return productService.searchByCategory(category);
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(
            @RequestParam int limit) {

        return productService.getLowStockProducts(limit);
    }
}