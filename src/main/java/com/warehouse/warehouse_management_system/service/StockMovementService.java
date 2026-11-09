package com.warehouse.warehouse_management_system.service;

import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.model.StockMovement;
import com.warehouse.warehouse_management_system.repository.StockMovementRepository;

import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing stock movement history.
 *
 * Handles recording stock movements and retrieving
 * the movement history for warehouse products.
 */
@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    /**
     * Creates a new StockMovementService.
     *
     * @param stockMovementRepository repository for stock movements
     */
    public StockMovementService(
            StockMovementRepository stockMovementRepository) {

        this.stockMovementRepository = stockMovementRepository;
    }

    /**
     * Records a new stock movement in the database.
     *
     * @param product the product involved in the movement
     * @param movementType the movement type, IN or OUT
     * @param quantity the number of products moved
     * @return the saved stock movement
     */
    public StockMovement recordMovement(
            Product product,
            String movementType,
            int quantity) {

        StockMovement movement = new StockMovement(
                product,
                movementType,
                quantity
        );

        return stockMovementRepository.save(movement);
    }

    /**
     * Retrieves all stock movements for a specific product.
     *
     * Results are ordered by creation date,
     * with the newest movements first.
     *
     * @param productId the ID of the product
     * @return a list of stock movements for the product
     */
    public List<StockMovement> getMovementsByProductId(
            Long productId) {

        return stockMovementRepository
                .findByProductIdOrderByCreatedAtDesc(productId);
    }
}