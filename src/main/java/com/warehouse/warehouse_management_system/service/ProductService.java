
package com.warehouse.warehouse_management_system.service;

import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

import com.warehouse.warehouse_management_system.repository.InventoryRepository;
import com.warehouse.warehouse_management_system.model.Inventory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class responsible for managing product operations
 * in the Warehouse Management System.
 *
 * Provides business logic for creating, retrieving, updating,
 * and deleting products.
 *
 * Validates SKU uniqueness to prevent duplicate products
 * and handles errors when products cannot be found.
 *
 * Uses ProductRepository to access and manage product data
 * in the database.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    
/**
 * Creates a ProductService with the required repositories.
 *
 * @param productRepository repository for product operations
 * @param inventoryRepository repository for inventory operations
 */
public ProductService(
    ProductRepository productRepository,
    InventoryRepository inventoryRepository) {

this.productRepository = productRepository;
this.inventoryRepository = inventoryRepository;
}


    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


/**
 * Saves a new product and automatically creates
 * an inventory record with an initial quantity of zero.
 *
 * Both operations are performed in a single transaction
 * to ensure data consistency.
 *
 * @param product the product to save
 * @return the saved product
 * @throws ResponseStatusException if the SKU already exists (409)
 */
@Transactional
public Product saveProduct(Product product) {

    if (productRepository.existsBySku(product.getSku())) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Product with this SKU already exists"
        );
    }

    // Save the new product
    Product savedProduct = productRepository.save(product);

    // Create an inventory record with zero stock
    Inventory inventory = new Inventory(savedProduct, 0);

    // Save the inventory record
    inventoryRepository.save(inventory);

    return savedProduct;
}

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the ID of the product
     * @return the requested product
     * @throws ResponseStatusException if the product is not found (404)
     */
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found with id: " + id
        ));
    }

    /**
     * Updates an existing product with new information.
     *
     * Checks whether the product exists and ensures that
     * the updated SKU is not used by another product.
     *
     * Updates the product name, SKU, description, and price.
     *
     * @param id the ID of the product to update
     * @param updatedProduct the new product information
     * @return the updated product
     * @throws ResponseStatusException if the product is not found (404)
     *         or the SKU is already used by another product (409)
     */   
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct = getProductById(id);

        // Kontrollera om SKU redan används av en annan produkt
        if (productRepository.existsBySkuAndIdNot(updatedProduct.getSku(), id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Product with this SKU already exists"
        );
        }

        // Update product information
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());

        // Save the updated product
        return productRepository.save(existingProduct);
    }

    /**
     * Deletes a product from the database.
     *
     * Checks whether the product exists before deleting it.
     *
     * @param id the ID of the product to delete
     * @throws ResponseStatusException if the product is not found (404)
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}