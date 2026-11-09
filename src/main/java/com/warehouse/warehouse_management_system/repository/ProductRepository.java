
package com.warehouse.warehouse_management_system.repository;

import com.warehouse.warehouse_management_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface responsible for accessing and managing
 * product data in the Warehouse Management System.
 *
 * Extends JpaRepository to provide built-in database operations
 * such as saving, retrieving, updating, and deleting products.
 *
 * Includes custom methods to check whether a product SKU
 * already exists in the database.
 *
 * Spring Data JPA automatically generates database queries
 * based on the method names.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Checks whether a product with the specified SKU exists.
     *
     * Used to prevent duplicate SKUs when creating new products.
     *
     * @param sku the SKU to check
     * @return true if the SKU already exists, otherwise false
     */
    boolean existsBySku(String sku);
    /**
     * Checks whether another product has the specified SKU,
     * excluding the product with the given ID.
     *
     * Used to prevent duplicate SKUs when updating an existing product.
     *
     * @param sku the SKU to check
     * @param id the ID of the product to exclude
     * @return true if another product has the same SKU, otherwise false
     */
    boolean existsBySkuAndIdNot(String sku, Long id);
}
