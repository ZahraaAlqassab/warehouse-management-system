
package com.warehouse.warehouse_management_system.model;

import jakarta.persistence.*;

/**
 * Represents the inventory information for a product in the
 * Warehouse Management System.
 *
 * This entity stores the stock quantity of a specific product
 * and is mapped to the "inventory" table in the database.
 *
 * Each inventory record is associated with one product
 * through a one-to-one relationship.
 *
 * The stock quantity is initialized to zero by default.
 */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 0;

    /**
     * Default constructor required by JPA.
     */
    public Inventory() {
    }

    /**
     * Creates a new inventory record for a product.
     *
     * @param product the product associated with this inventory
     * @param quantity the initial stock quantity
     */
    public Inventory(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Returns the unique identifier of the inventory record.
     *
     * @return the inventory ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the product associated with this inventory.
     *
     * @return the associated product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product associated with this inventory.
     *
     * @param product the product to associate with this inventory
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Returns the current stock quantity.
     *
     * @return the number of units in stock
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Updates the stock quantity of this inventory.
     *
     * @param quantity the new stock quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}