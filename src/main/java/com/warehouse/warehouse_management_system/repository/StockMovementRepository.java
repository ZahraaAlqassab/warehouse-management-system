
package com.warehouse.warehouse_management_system.repository;

import com.warehouse.warehouse_management_system.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for managing stock movement records.
 *
 * Provides database operations for saving, retrieving,
 * and managing stock movement history.
 *
 * Extends JpaRepository to use built-in CRUD operations.
 */
public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    /**
     * Finds all stock movements for a specific product.
     *
     * The movements are ordered by creation date,
     * with the newest movements first.
     *
     * @param productId the ID of the product
     * @return a list of stock movements for the product
     */
    List<StockMovement> findByProductIdOrderByCreatedAtDesc(
            Long productId
    );
}