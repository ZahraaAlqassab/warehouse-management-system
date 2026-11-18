
package com.warehouse.warehouse_management_system.controller;

import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for managing inventory in the
 * Warehouse Management System.
 *
 * Provides API endpoints to retrieve inventory information,
 * add stock, and remove stock for specific products.
 *
 * Receives HTTP requests and delegates inventory operations
 * and business logic to InventoryService.
 *
 * All endpoints are available under /api/inventory.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    // Service responsible for inventory operations
    private final InventoryService inventoryService;

    // Injects InventoryService through the constructor
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

/**
 * Retrieves inventory information for a specific product.
 *
 * @param productId the ID of the product
 * @return the inventory information for the product
 */
@GetMapping("/{productId}")
public Inventory getInventory(@PathVariable Long productId) {

    // Retrieves inventory information from the service
    return inventoryService.getInventoryByProductId(productId);
}

/**
 * Adds stock to a specific product.
 *
 * @param productId the ID of the product
 * @param quantity the quantity to add
 * @return the updated inventory information
 */
@PostMapping("/{productId}/add")
public Inventory addStock(
        @PathVariable Long productId,
        @RequestParam int quantity) {

    // Increases the stock quantity for the specified product        
    return inventoryService.addStock(productId, quantity);
}

/**
 * Removes stock from a specific product.
 *
 * @param productId the ID of the product
 * @param quantity the quantity to remove
 * @return the updated inventory information
 */
@PostMapping("/{productId}/remove")
public Inventory removeStock(
        @PathVariable Long productId,
        @RequestParam int quantity) {

    // Decreases the stock quantity for the specified product
    return inventoryService.removeStock(productId, quantity);
}

/**
 * Retrieves all inventory records with low stock.
 *
 * Returns products whose stock quantity is greater
 * than zero and less than or equal to their
 * configured low stock threshold.
 *
 * @return a list of inventory records with low stock
 */
@GetMapping("/low-stock")
public List<Inventory> getLowStockInventory() {
    return inventoryService.getLowStockInventory();
}

/**
 * Retrieves all inventory records that are out of stock.
 *
 * Returns inventory records where the stock
 * quantity is equal to zero.
 *
 * @return a list of inventory records with zero stock
 */
@GetMapping("/out-of-stock")
public List<Inventory> getOutOfStockInventory() {
    return inventoryService.getOutOfStockInventory();
}
}