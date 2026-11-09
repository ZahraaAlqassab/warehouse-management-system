
package com.warehouse.warehouse_management_system.controller;

import com.warehouse.warehouse_management_system.model.StockMovement;
import com.warehouse.warehouse_management_system.service.StockMovementService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for retrieving stock movement history.
 *
 * Provides an API endpoint to view all stock movements
 * for a specific warehouse product.
 */
@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    /**
     * Creates a new StockMovementController.
     *
     * @param stockMovementService service for managing stock history
     */
    public StockMovementController(
            StockMovementService stockMovementService) {

        this.stockMovementService = stockMovementService;
    }

    /**
     * Retrieves the stock movement history for a product.
     *
     * Movements are returned in descending order
     * by creation date, with the newest first.
     *
     * @param productId the ID of the product
     * @return a list of stock movements for the product
     */
    @GetMapping("/{productId}")
    public List<StockMovement> getMovementsByProductId(
            @PathVariable Long productId) {

        return stockMovementService
                .getMovementsByProductId(productId);
    }
}