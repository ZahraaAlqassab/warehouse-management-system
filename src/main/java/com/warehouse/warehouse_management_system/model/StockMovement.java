
package com.warehouse.warehouse_management_system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a stock movement in the warehouse.
 *
 * Each movement records whether products were added
 * or removed, the quantity, and the date and time.
 */
@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "movement_type", nullable = false)
    private String movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor required by JPA.
     */
    public StockMovement() {
    }

    /**
     * Creates a new stock movement.
     *
     * @param product the product being updated
     * @param movementType IN or OUT
     * @param quantity number of products added or removed
     */
    public StockMovement(
            Product product,
            String movementType,
            Integer quantity) {

        this.product = product;
        this.movementType = movementType;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}