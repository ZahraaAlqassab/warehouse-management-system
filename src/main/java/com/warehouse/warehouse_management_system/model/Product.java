
package com.warehouse.warehouse_management_system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

/**
 * Represents a product in the Warehouse Management System.
 *
 * This entity stores product information such as name, SKU,
 * description, and price. It is mapped to the "products"
 * table in the database.
 *
 * Each product has a unique SKU used for identification.
 * Product data is validated to ensure that required fields
 * are provided and the price is greater than zero.
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "SKU is required")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Price must be greater than zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Default constructor required by JPA.
     */
    public Product() {}

    /**
     * Creates a new product with the provided information.
     *
     * @param name the name of the product
     * @param sku the unique stock keeping unit
     * @param description the description of the product
     * @param price the price of the product
     */
    public Product(String name, String sku,
               String description, BigDecimal price) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
    }

    /**
     * Returns the unique identifier of the product.
     *
     * @return the product ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the name of the product.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the product.
     *
     * @param name the new product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the unique SKU of the product.
     *
     * @return the product SKU
     */
    public String getSku() {
        return sku;
    }

    /**
     * Updates the SKU of the product.
     *
     * @param sku the new product SKU
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

     /**
     * Returns the description of the product.
     *
     * @return the product description
     */
    public String getDescription() {
        return description;
    }

     /**
     * Updates the description of the product.
     *
     * @param description the new product description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the price of the product.
     *
     * @return the product price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Updates the price of the product.
     *
     * @param price the new product price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}