
package com.warehouse.warehouse_management_system.controller;

import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.service.InventoryService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // Constructor injection
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Get inventory by product ID
    @GetMapping("/{productId}")
    public Inventory getInventory(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    // Add stock to a product
    @PostMapping("/{productId}/add")
    public Inventory addStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        return inventoryService.addStock(productId, quantity);
    }

    // Remove stock from a product
    @PostMapping("/{productId}/remove")
    public Inventory removeStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        return inventoryService.removeStock(productId, quantity);
    }
}

