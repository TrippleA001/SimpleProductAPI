package com.myjavainternship.productapi.service;

import com.myjavainternship.productapi.exception.ResourceNotFoundException;
import com.myjavainternship.productapi.model.Product;
import com.myjavainternship.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Marks this class as a Spring service component
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired // Dependency injection of the repository (constructor injection is preferred)
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    public Product addProduct(Product product) {
        // Potential future logic: Check for duplicate names, etc.
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = getProductById(id); // Reuse getById which includes not found check

        existingProduct.setName(productDetails.getName());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setQuantity(productDetails.getQuantity());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id); // Ensure product exists before trying to delete
        productRepository.delete(existingProduct);
        // Or use deleteById directly, but checking existence first provides better feedback
        // if (!productRepository.existsById(id)) {
        //     throw new ResourceNotFoundException("Product", "id", id);
        // }
        // productRepository.deleteById(id);
    }
}