
package com.warehouse.warehouse_management_system.service;

import com.warehouse.warehouse_management_system.model.Product;
import com.warehouse.warehouse_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

public Product saveProduct(Product product) {

    if (productRepository.existsBySku(product.getSku())) {
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Product with this SKU already exists"
        );
    }

    return productRepository.save(product);
}

   public Product getProductById(Long id) {
        return productRepository.findById(id)
         .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Product not found with id: " + id
        ));
}

public Product updateProduct(Long id, Product updatedProduct) {

    Product existingProduct = getProductById(id);

    // Kontrollera om SKU redan används av en annan produkt
    if (productRepository.existsBySkuAndIdNot(updatedProduct.getSku(), id)) {
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Product with this SKU already exists"
        );
    }

    existingProduct.setName(updatedProduct.getName());
    existingProduct.setSku(updatedProduct.getSku());
    existingProduct.setDescription(updatedProduct.getDescription());
    existingProduct.setPrice(updatedProduct.getPrice());

    return productRepository.save(existingProduct);
}

public void deleteProduct(Long id) {
    Product product = getProductById(id);
    productRepository.delete(product);
}
}