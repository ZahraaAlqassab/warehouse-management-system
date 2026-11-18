package com.warehouse.warehouse_management_system.controller;

import com.warehouse.warehouse_management_system.dto.InventoryDashboardResponse;
import com.warehouse.warehouse_management_system.service.InventoryDashboardService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the warehouse inventory dashboard.
 *
 * Provides an API endpoint for retrieving summary
 * statistics about products and inventory levels.
 */
@RestController
@RequestMapping("/api/dashboard")
public class InventoryDashboardController {

    private final InventoryDashboardService dashboardService;

    /**
     * Creates a new InventoryDashboardController.
     *
     * @param dashboardService service for generating dashboard statistics
     */
    public InventoryDashboardController(
            InventoryDashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    /**
     * Retrieves summary statistics for the inventory dashboard.
     *
     * @return dashboard statistics including total products,
     *         low stock products, out of stock products,
     *         and total stock quantity
     */
    @GetMapping
    public InventoryDashboardResponse getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }
}