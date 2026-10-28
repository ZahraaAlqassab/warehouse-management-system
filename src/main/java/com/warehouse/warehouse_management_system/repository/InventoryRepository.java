
package com.warehouse.warehouse_management_system.repository;

import com.warehouse.warehouse_management_system.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface responsible for accessing and managing
 * inventory data in the Warehouse Management System.
 *
 * Extends JpaRepository to provide built-in database operations
 * such as saving, retrieving, updating, and deleting inventory records.
 *
 * Uses Spring Data JPA to communicate with the database
 * without requiring manual SQL queries.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Finds the inventory record associated with a specific product.
     *
     * Spring Data JPA automatically generates the database query
     * based on the method name.
     *
     * @param productId the ID of the product
     * @return an Optional containing the inventory record if found,
     *         or an empty Optional if no inventory exists
     */    
    Optional<Inventory> findByProductId(Long productId);
}