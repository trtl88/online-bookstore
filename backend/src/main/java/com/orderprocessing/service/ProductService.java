package com.orderprocessing.service;

import com.orderprocessing.model.Inventory;
import com.orderprocessing.model.Product;
import com.orderprocessing.model.Supplier;
import com.orderprocessing.repository.InventoryRepository;
import com.orderprocessing.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final SupplierService supplierService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(Product product, Long supplierId) {
        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            product.setSupplier(supplier);
        }
        
        Product savedProduct = productRepository.save(product);
        
        // Create initial inventory entry
        Inventory inventory = new Inventory();
        inventory.setProduct(savedProduct);
        inventory.setQuantityInStock(0);
        inventory.setReorderLevel(10);
        inventoryRepository.save(inventory);
        
        return savedProduct;
    }

    public Product updateProduct(Long id, Product productDetails, Long supplierId) {
        Product product = getProductById(id);
        
        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setUnitPrice(productDetails.getUnitPrice());
        
        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            product.setSupplier(supplier);
        }
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplier_SupplierId(supplierId);
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByUnitPriceBetween(minPrice, maxPrice);
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }
}
