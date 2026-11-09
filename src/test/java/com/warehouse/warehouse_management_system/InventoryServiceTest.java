
package com.warehouse.warehouse_management_system;

import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.InventoryRepository;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import com.warehouse.warehouse_management_system.service.InventoryService;
import com.warehouse.warehouse_management_system.service.StockMovementService;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the InventoryService class.
 *
 * Verifies inventory operations using JUnit 5 and Mockito.
 * Uses mock repositories to test business logic without
 * connecting to a real database.
 */
public class InventoryServiceTest {

    /**
     * Tests that inventory information can be retrieved
     * using a product ID.
     *
     * Verifies that the correct stock quantity is returned
     * and that the repository method is called.
     */
    @Test
    void shouldGetInventoryByProductId() {

        // Arrange
        InventoryRepository inventoryRepository =
                mock(InventoryRepository.class);

        ProductRepository productRepository =
                mock(ProductRepository.class);

        
     /*
      * Mock the stock movement service to test inventory
      * operations without writing movement records to the database.
      */
        StockMovementService stockMovementService =
        mock(StockMovementService.class);

      // Create the inventory service with all dependencies
        InventoryService service = new InventoryService(
                inventoryRepository,
                productRepository,
                stockMovementService);


        Product product = new Product();
        Inventory inventory = new Inventory(product, 10);

        when(inventoryRepository.findByProductId(1L))
                .thenReturn(Optional.of(inventory));

        // Act
        Inventory result = service.getInventoryByProductId(1L);

        // Assert
        assertEquals(10, result.getQuantity());

        verify(inventoryRepository).findByProductId(1L);
    }



/**
 * Tests that stock can be added to an existing inventory.
 *
 * Verifies that the stock quantity increases correctly,
 * the updated inventory is saved, and an incoming
 * stock movement is recorded.
 */
@Test
void shouldAddStock() {

    // Arrange
    InventoryRepository inventoryRepository =
            mock(InventoryRepository.class);

    ProductRepository productRepository =
            mock(ProductRepository.class);

            StockMovementService stockMovementService =
            mock(StockMovementService.class);
    
        // Create the service with all required dependencies
        InventoryService service = new InventoryService(
            inventoryRepository,
            productRepository,
            stockMovementService
    );

    Product product = new Product();
    Inventory inventory = new Inventory(product, 10);

    when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));

    when(inventoryRepository.findByProductId(1L))
            .thenReturn(Optional.of(inventory));

    when(inventoryRepository.save(inventory))
            .thenReturn(inventory);

    // Act
    Inventory result = service.addStock(1L, 5);

    // Assert
    assertEquals(15, result.getQuantity());

    verify(inventoryRepository).save(inventory);

    // Verify that an incoming stock movement was recorded
    verify(stockMovementService).recordMovement(
        product,
        "IN",
        5);
}

/**
 * Tests that stock can be removed from an existing inventory.
 *
 * Verifies that the stock quantity decreases correctly,
 * the updated inventory is saved, and an outgoing
 * stock movement is recorded.
 */
@Test
void shouldRemoveStock() {

    // Arrange
    InventoryRepository inventoryRepository =
            mock(InventoryRepository.class);

    ProductRepository productRepository =
            mock(ProductRepository.class);

            StockMovementService stockMovementService =
            mock(StockMovementService.class);
    
    // Create the service with all required dependencies
    InventoryService service = new InventoryService(
            inventoryRepository,
            productRepository,
            stockMovementService
    );

    Product product = new Product();
    Inventory inventory = new Inventory(product, 10);

    when(inventoryRepository.findByProductId(1L))
            .thenReturn(Optional.of(inventory));

    when(inventoryRepository.save(inventory))
            .thenReturn(inventory);

    // Act
    Inventory result = service.removeStock(1L, 3);

    // Assert
    assertEquals(7, result.getQuantity());

    verify(inventoryRepository).save(inventory);

    // Verify that an outgoing stock movement was recorded
    verify(stockMovementService).recordMovement(
        product,
        "OUT",
        3);
}

/**
 * Tests that stock cannot be removed when the
 * requested quantity exceeds the available stock.
 *
 * Verifies that the service throws an exception
 * and does not save the inventory.
 */
@Test
void shouldNotRemoveMoreStockThanAvailable() {

    // Arrange
    InventoryRepository inventoryRepository =
            mock(InventoryRepository.class);

    ProductRepository productRepository =
            mock(ProductRepository.class);

            StockMovementService stockMovementService =
            mock(StockMovementService.class);
    
    // Create the service with all required dependencies
    InventoryService service = new InventoryService(
            inventoryRepository,
            productRepository,
            stockMovementService
    );

    Product product = new Product();
    Inventory inventory = new Inventory(product, 10);

    when(inventoryRepository.findByProductId(1L))
            .thenReturn(Optional.of(inventory));

    // Act & Assert
    ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.removeStock(1L, 20)
    );

    assertEquals(
            HttpStatus.CONFLICT,
            exception.getStatusCode()
    );

    assertEquals(10, inventory.getQuantity());

    verify(inventoryRepository, never()).save(any(Inventory.class));
}

/**
 * Tests that adding zero or negative stock is not allowed.
 *
 * Verifies that the service throws BAD_REQUEST
 * and does not save any inventory.
 */
@Test
void shouldRejectInvalidStockQuantity() {

    // Arrange
    InventoryRepository inventoryRepository =
            mock(InventoryRepository.class);

    ProductRepository productRepository =
            mock(ProductRepository.class);

            StockMovementService stockMovementService =
            mock(StockMovementService.class);
    
    // Create the service with all required dependencies
    InventoryService service = new InventoryService(
            inventoryRepository,
            productRepository,
            stockMovementService
    );

    // Act & Assert - Zero quantity
    ResponseStatusException zeroException = assertThrows(
            ResponseStatusException.class,
            () -> service.addStock(1L, 0)
    );

    assertEquals(
            HttpStatus.BAD_REQUEST,
            zeroException.getStatusCode()
    );

    // Act & Assert - Negative quantity
    ResponseStatusException negativeException = assertThrows(
            ResponseStatusException.class,
            () -> service.addStock(1L, -5)
    );

    assertEquals(
            HttpStatus.BAD_REQUEST,
            negativeException.getStatusCode()
    );

    // Verify that no inventory was saved
    verify(inventoryRepository, never()).save(any(Inventory.class));
}

/**
 * Tests that removing zero or negative stock is not allowed.
 *
 * Verifies that the service throws BAD_REQUEST
 * and does not save any inventory.
 */
@Test
void shouldRejectInvalidRemoveQuantity() {

    // Arrange
    InventoryRepository inventoryRepository =
            mock(InventoryRepository.class);

    ProductRepository productRepository =
            mock(ProductRepository.class);

            StockMovementService stockMovementService =
            mock(StockMovementService.class);
    
    // Create the service with all required dependencies
    InventoryService service = new InventoryService(
            inventoryRepository,
            productRepository,
            stockMovementService
    );

    // Act & Assert - Zero quantity
    ResponseStatusException zeroException = assertThrows(
            ResponseStatusException.class,
            () -> service.removeStock(1L, 0)
    );

    assertEquals(
            HttpStatus.BAD_REQUEST,
            zeroException.getStatusCode()
    );

    // Act & Assert - Negative quantity
    ResponseStatusException negativeException = assertThrows(
            ResponseStatusException.class,
            () -> service.removeStock(1L, -3)
    );

    assertEquals(
            HttpStatus.BAD_REQUEST,
            negativeException.getStatusCode()
    );

    // Verify that no inventory was saved
    verify(inventoryRepository, never())
            .save(any(Inventory.class));
}
}