package com.warehouse.warehouse_management_system;

import com.warehouse.warehouse_management_system.model.Inventory;
import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.InventoryRepository;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import com.warehouse.warehouse_management_system.service.ProductService;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProductService class.
 *
 * Verifies product creation, retrieval, updating, deletion,
 * SKU validation, and automatic inventory creation.
 *
 * Uses JUnit 5 and Mockito to test business logic
 * without connecting to a real database.
 */
public class ProductServiceTest {

    /**
     * Tests that saving a new product automatically
     * creates an inventory record with zero quantity.
     */
    @Test
    void shouldSaveProduct() {

        ProductRepository repository = mock(ProductRepository.class);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);

        ProductService service =
                new ProductService(repository, inventoryRepository);

        Product product = new Product(
                "Laptop",
                "LAP-001",
                "Dell laptop",
                new BigDecimal("7999.99")
        );

        when(repository.save(product)).thenReturn(product);

        Product result = service.saveProduct(product);

        assertEquals("Laptop", result.getName());

        verify(repository).save(product);

        ArgumentCaptor<Inventory> inventoryCaptor =
                ArgumentCaptor.forClass(Inventory.class);

        verify(inventoryRepository).save(inventoryCaptor.capture());

        Inventory savedInventory = inventoryCaptor.getValue();

        assertSame(product, savedInventory.getProduct());
        assertEquals(0, savedInventory.getQuantity());
    }

    /**
     * Tests that saving a product with a duplicate SKU
     * throws HTTP 409 Conflict and does not create inventory.
     */
    @Test
    void shouldThrowExceptionWhenSkuAlreadyExists() {

        ProductRepository repository = mock(ProductRepository.class);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);

        ProductService service =
                new ProductService(repository, inventoryRepository);

        Product product = new Product(
                "Laptop",
                "LAP-001",
                "Dell laptop",
                new BigDecimal("7999.00")
        );

        when(repository.existsBySku("LAP-001")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.saveProduct(product)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals(
                "Product with this SKU already exists",
                exception.getReason()
        );

        verify(repository, never()).save(any(Product.class));
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    /**
     * Tests that an existing product can be retrieved
     * successfully using its ID.
     */
    @Test
    void shouldGetProductById() {

        ProductRepository repository = mock(ProductRepository.class);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);

        ProductService service =
                new ProductService(repository, inventoryRepository);

        Product product = new Product(
                "Laptop",
                "LAP-001",
                "Dell laptop",
                new BigDecimal("7999.00")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = service.getProductById(1L);

        assertEquals("Laptop", result.getName());
        assertEquals("LAP-001", result.getSku());

        verify(repository).findById(1L);
    }

    /**
     * Tests that requesting a nonexistent product
     * throws HTTP 404 Not Found.
     */
    @Test
    void shouldThrowExceptionWhenProductNotFound() {

        ProductRepository repository = mock(ProductRepository.class);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);

        ProductService service =
                new ProductService(repository, inventoryRepository);

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getProductById(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(
                "Product not found with id: 999",
                exception.getReason()
        );

        verify(repository).findById(999L);
    }

    /**
     * Tests that an existing product can be updated
     * with new information.
     */
    @Test
    void shouldUpdateProduct() {

        ProductRepository repository = mock(ProductRepository.class);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);

        ProductService service =
                new ProductService(repository, inventoryRepository);

        Product existingProduct = new Product(
                "Laptop",
                "LAP-001",
                "Old laptop",
                new BigDecimal("7999.00")
        );

        Product updatedProduct = new Product(
                "Dell Laptop",
                "LAP-001",
                "Updated laptop",
                new BigDecimal("7500.00")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        when(repository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result = service.updateProduct(1L, updatedProduct);

        assertEquals("Dell Laptop", result.getName());
        assertEquals("Updated laptop", result.getDescription());
        assertEquals(new BigDecimal("7500.00"), result.getPrice());

        verify(repository).save(existingProduct);
    }

    /**
     * Tests that an existing product can be deleted
     * successfully from the repository.
     */
    @Test
    void shouldDeleteProduct() {

        ProductRepository repository = mock(ProductRepository.class);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);

        ProductService service =
                new ProductService(repository, inventoryRepository);

        Product product = new Product(
                "Laptop",
                "LAP-001",
                "Dell laptop",
                new BigDecimal("7999.00")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        service.deleteProduct(1L);

        verify(repository).delete(product);
    }
}
