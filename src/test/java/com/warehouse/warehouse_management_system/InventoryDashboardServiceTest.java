package com.warehouse.warehouse_management_system;

import com.warehouse.warehouse_management_system.dto.InventoryDashboardResponse;
import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.InventoryRepository;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import com.warehouse.warehouse_management_system.service.InventoryDashboardService;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for InventoryDashboardService.
 *
 * Verifies that dashboard statistics are calculated
 * correctly using mocked repository data.
 */
public class InventoryDashboardServiceTest {

    /**
     * Tests that the dashboard summary contains
     * the correct product counts and stock quantity.
     */
    @Test
    void shouldGetDashboardSummary() {

        // Arrange: Create mock repositories
        ProductRepository productRepository =
                mock(ProductRepository.class);

        InventoryRepository inventoryRepository =
                mock(InventoryRepository.class);

        InventoryDashboardService service =
                new InventoryDashboardService(
                        productRepository,
                        inventoryRepository
                );

        Product product1 = new Product();
        Product product2 = new Product();

        Inventory inventory1 = new Inventory(product1, 5);
        Inventory inventory2 = new Inventory(product2, 0);

        when(productRepository.count()).thenReturn(2L);

        when(inventoryRepository.findLowStockInventory())
                .thenReturn(List.of(inventory1));

        when(inventoryRepository.findByQuantity(0))
                .thenReturn(List.of(inventory2));

        when(inventoryRepository.findAll())
                .thenReturn(List.of(inventory1, inventory2));

        // Act: Generate dashboard summary
        InventoryDashboardResponse result =
                service.getDashboardSummary();

        // Assert: Verify dashboard statistics
        assertEquals(2, result.totalProducts());
        assertEquals(1, result.lowStockProducts());
        assertEquals(1, result.outOfStockProducts());
        assertEquals(5, result.totalStockQuantity());

        verify(productRepository).count();
        verify(inventoryRepository).findLowStockInventory();
        verify(inventoryRepository).findByQuantity(0);
        verify(inventoryRepository).findAll();
    }
}