
package com.warehouse.warehouse_management_system.controller;

import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

/**
 * REST controller responsible for managing products in the
 * Warehouse Management System.
 *
 * Provides API endpoints to create, retrieve, update, and delete
 * products using standard HTTP methods (GET, POST, PUT, DELETE).
 *
 * Receives HTTP requests, validates incoming product data,
 * and delegates business logic to ProductService.
 *
 * All endpoints are available under /api/products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

     /**
     * Creates a ProductController with the required product service.
     *
     * @param productService the service responsible for product operations
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Creates a new product after validating its data.
     *
     * @param product the product to create
     * @return the newly created product
     */
    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        return productService.saveProduct(product);
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the ID of the product
     * @return the requested product
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * Updates an existing product with validated data.
     *
     * @param id the ID of the product to update
     * @param product the updated product information
     * @return the updated product
     */
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
    
        return productService.updateProduct(id, product);
    }

    /**
     * Deletes a product by its ID.
     *
     * Returns HTTP 204 No Content after successful deletion.
     *
     * @param id the ID of the product to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}