
package com.warehouse.warehouse_management_system.service;

import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.repository.InventoryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    // Constructor injection
    public InventoryService(
           InventoryRepository inventoryRepository,
           ProductRepository productRepository) {

        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
}

    // Get inventory by product ID
    public Inventory getInventoryByProductId(Long productId) {

        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Inventory not found for product ID: " + productId
                ));
    }


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
