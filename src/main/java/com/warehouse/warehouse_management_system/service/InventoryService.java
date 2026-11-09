
package com.warehouse.warehouse_management_system.service;

import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.repository.InventoryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class responsible for managing inventory operations
 * in the Warehouse Management System.
 *
 * Provides business logic for retrieving inventory information,
 * adding stock, and removing stock from products.
 *
 * Validates stock quantities, checks product availability,
 * and prevents stock levels from becoming negative.
 *
 * Uses InventoryRepository and ProductRepository to access
 * and update data in the database.
 */
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

       /**
       * Creates an InventoryService with the required repositories.
       *
       * @param inventoryRepository the repository for inventory operations
       * @param productRepository the repository for product operations
       */
        public InventoryService(
           InventoryRepository inventoryRepository,
           ProductRepository productRepository) {

        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
}

       /**
       * Retrieves inventory information for a specific product.
       *
       * @param productId the ID of the product
       * @return the inventory information for the product
       * @throws ResponseStatusException if the inventory is not found (404)
       */
       public Inventory getInventoryByProductId(Long productId) {

           return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Inventory not found for product ID: " + productId
                ));
        }

       /**
       * Adds stock to a specific product.
       *
       * Validates the quantity and checks whether the product exists.
       * Creates a new inventory record if one does not already exist.
       *
       * @param productId the ID of the product
       * @param quantity the quantity to add
       * @return the updated inventory information
       * @throws ResponseStatusException if the quantity is invalid (400)
       *         or the product is not found (404)
       */
       @Transactional
        public Inventory addStock(Long productId, int quantity) {

         // Validate incoming quantity
            if (quantity <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Quantity must be greater than zero"
                );
        }

        // Find the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + productId
                ));

        // Find existing inventory or create a new one
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> new Inventory(product, 0));

        // Increase stock quantity
        inventory.setQuantity(
                Math.addExact(inventory.getQuantity(), quantity)
        );

        // Save updated inventory
        return inventoryRepository.save(inventory);
}

     /**
     * Removes stock from a specific product.
     *
     * Validates the quantity, checks whether the inventory exists,
     * and ensures that enough stock is available before removing it.
     *
     * Prevents the stock quantity from becoming negative.
     *
     * @param productId the ID of the product
     * @param quantity the quantity to remove
     * @return the updated inventory information
     * @throws ResponseStatusException if the quantity is invalid (400),
     *         the inventory is not found (404),
     *         or there is insufficient stock (409)
     */
     @Transactional
     public Inventory removeStock(Long productId, int quantity) {

        // Validate outgoing quantity
        if (quantity <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Quantity must be greater than zero"
        );
        }

        // Find the inventory for the product
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Inventory not found for product ID: " + productId
            ));

        // Check if enough stock is available
        if (inventory.getQuantity() < quantity) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Not enough stock available"
        );
        }

        // Decrease stock quantity
        inventory.setQuantity(inventory.getQuantity() - quantity);

        // Save updated inventory
        return inventoryRepository.save(inventory);
}
}