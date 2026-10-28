
package com.warehouse.warehouse_management_system.repository;

import com.warehouse.warehouse_management_system.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Find inventory by product ID
    Optional<Inventory> findByProductId(Long productId);

}
