package com.warehouse.warehouse_management_system.service;

import com.warehouse.warehouse_management_system.dto.InventoryDashboardResponse;
import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.repository.InventoryRepository;
import com.warehouse.warehouse_management_system.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for generating inventory dashboard statistics.
 *
 * Retrieves warehouse data and calculates summary information
 * about products, stock quantities, and stock availability.
 */
@Service
public class InventoryDashboardService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * Creates a new InventoryDashboardService.
     *
     * @param productRepository repository for product data
     * @param inventoryRepository repository for inventory data
     */
    public InventoryDashboardService(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository) {

        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Generates summary statistics for the inventory dashboard.
     *
     * @return dashboard statistics containing product counts,
     *         low stock counts, out of stock counts,
     *         and total stock quantity
     */
    public InventoryDashboardResponse getDashboardSummary() {

        long totalProducts = productRepository.count();

        long lowStockProducts =
                inventoryRepository.findLowStockInventory().size();

        long outOfStockProducts =
                inventoryRepository.findByQuantity(0).size();

        List<Inventory> inventoryRecords =
                inventoryRepository.findAll();

        long totalStockQuantity = inventoryRecords.stream()
                .mapToLong(Inventory::getQuantity)
                .sum();

        return new InventoryDashboardResponse(
                totalProducts,
                lowStockProducts,
                outOfStockProducts,
                totalStockQuantity
        );
    }
}