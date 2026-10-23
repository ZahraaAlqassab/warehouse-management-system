package com.warehouse.warehouse_management_system;

import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import com.warehouse.warehouse_management_system.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

public class ProductServiceTest {
    
    @Test
    void shouldSaveProduct(){
        //1. Create a fake repository
        ProductRepository repository = mock(ProductRepository.class);

        // 2. Create our service
        ProductService service = new ProductService(repository);

        // 3. create a product
        Product product = new Product(
            "Laptop",
            "LAP-001",
            "Dell laptop",
            new BigDecimal("7999.99")
        );

        // 4. Define the mock repository behavior
        when(repository.save(product)).thenReturn(product);

        // 5. Call the method being tested
        Product result = service.saveProduct(product);

        // 6. Verify the result
        assertEquals("Laptop", result.getName());
        verify(repository).save(product);
    }


@Test
void shouldThrowExceptionWhenSkuAlreadyExists() {

    // Arrange: Create a mock repository
    ProductRepository repository = mock(ProductRepository.class);
    ProductService service = new ProductService(repository);

    // Create a product with an existing SKU
    Product product = new Product(
        "Laptop",
        "LAP-001",
        "Dell laptop",
        new BigDecimal("7999.00")
    );

    // Simulate that the SKU already exists
    when(repository.existsBySku("LAP-001")).thenReturn(true);

    // Act & Assert: Verify that an exception is thrown
    ResponseStatusException exception = assertThrows(
        ResponseStatusException.class,
        () -> service.saveProduct(product)
    );

    // Verify the HTTP status and error message
    assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    assertEquals(
        "Product with this SKU already exists",
        exception.getReason()
    );

    // Verify that the product was never saved
    verify(repository, never()).save(any(Product.class));
}


@Test
void shouldGetProductById() {

    // Arrange: Create a mock repository
    ProductRepository repository = mock(ProductRepository.class);
    ProductService service = new ProductService(repository);

    // Create a test product
    Product product = new Product(
        "Laptop",
        "LAP-001",
        "Dell laptop",
        new BigDecimal("7999.00")
    );

    // Simulate finding a product with ID 1
    when(repository.findById(1L))
        .thenReturn(Optional.of(product));

    // Act: Get the product by ID
    Product result = service.getProductById(1L);

    // Assert: Verify the product details
    assertEquals("Laptop", result.getName());
    assertEquals("LAP-001", result.getSku());

    // Verify that the repository was called
    verify(repository).findById(1L);
}


@Test
void shouldThrowExceptionWhenProductNotFound() {

    // Arrange: Create a mock repository
    ProductRepository repository = mock(ProductRepository.class);
    ProductService service = new ProductService(repository);

    // Simulate that product ID 999 does not exist
    when(repository.findById(999L))
        .thenReturn(Optional.empty());

    // Act & Assert: Verify that an exception is thrown
    ResponseStatusException exception = assertThrows(
        ResponseStatusException.class,
        () -> service.getProductById(999L)
    );

    // Verify the HTTP status and error message
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals(
        "Product not found with id: 999",
        exception.getReason()
    );

    // Verify that the repository was called
    verify(repository).findById(999L);
}


@Test
void shouldUpdateProduct() {

    // Arrange: Create a mock repository
    ProductRepository repository = mock(ProductRepository.class);
    ProductService service = new ProductService(repository);

    // Create an existing product
    Product existingProduct = new Product(
        "Laptop",
        "LAP-001",
        "Old laptop",
        new BigDecimal("7999.00")
    );

    // Create updated product information
    Product updatedProduct = new Product(
        "Dell Laptop",
        "LAP-001",
        "Updated laptop",
        new BigDecimal("7500.00")
    );

    // Simulate finding the existing product
    when(repository.findById(1L))
        .thenReturn(Optional.of(existingProduct));

    // Simulate saving the updated product
    when(repository.save(existingProduct))
        .thenReturn(existingProduct);

    // Act: Update the product
    Product result = service.updateProduct(1L, updatedProduct);

    // Assert: Verify the updated information
    assertEquals("Dell Laptop", result.getName());
    assertEquals("Updated laptop", result.getDescription());
    assertEquals(new BigDecimal("7500.00"), result.getPrice());

    // Verify that the repository saved the product
    verify(repository).save(existingProduct);
}

@Test
void shouldDeleteProduct() {

    // Arrange: Create a mock repository
    ProductRepository repository = mock(ProductRepository.class);
    ProductService service = new ProductService(repository);

    // Create a test product
    Product product = new Product(
        "Laptop",
        "LAP-001",
        "Dell laptop",
        new BigDecimal("7999.00")
    );

    // Simulate finding the product with ID 1
    when(repository.findById(1L))
        .thenReturn(Optional.of(product));

    // Act: Delete the product
    service.deleteProduct(1L);

    // Assert: Verify that the product was deleted
    verify(repository).delete(product);
}

}