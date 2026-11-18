
package com.warehouse.warehouse_management_system.dto;

/**
 * Represents summary information for the warehouse
 * inventory dashboard.
 *
 * Contains the total number of products, the number
 * of products with low stock, the number of products
 * that are out of stock, and the total stock quantity.
 *
 * @param totalProducts total number of registered products
 * @param lowStockProducts number of products with low stock
 * @param outOfStockProducts number of products with zero stock
 * @param totalStockQuantity total quantity of items in inventory
 */
public record InventoryDashboardResponse(
        long totalProducts,
        long lowStockProducts,
        long outOfStockProducts,
        long totalStockQuantity
) {
}
