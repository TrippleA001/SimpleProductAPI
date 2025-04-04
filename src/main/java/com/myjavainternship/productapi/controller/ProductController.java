package com.myjavainternship.productapi.controller;

import com.myjavainternship.productapi.model.Product;
import com.myjavainternship.productapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation; // For Swagger Documentation
import io.swagger.v3.oas.annotations.responses.ApiResponse; // For Swagger Documentation
import io.swagger.v3.oas.annotations.tags.Tag; // For Swagger Documentation
import jakarta.validation.Valid; // Enables validation on the request body
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller
@RequestMapping("/products") // Base path for all endpoints in this controller
@Tag(name = "Product Management API", description = "APIs for Creating, Reading, Updating, and Deleting Products") // Swagger Tag
public class ProductController {

    private final ProductService productService;

    @Autowired // Inject the service
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- Add a new product (POST /products) ---
    @PostMapping
    @Operation(summary = "Add a new product", description = "Creates a new product entry in the database.")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        //@Valid triggers validation defined in the Product entity
        Product newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED); // Return 201 Created status
    }

    // --- Get all products (GET /products) ---
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all available products.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products); // Return 200 OK status
    }

    // --- Get a product by ID (GET /products/{id}) ---
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a specific product using its unique ID.")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product); // Return 200 OK status
    }

    // --- Update a product (PUT /products/{id}) ---
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product", description = "Updates the details of a product specified by its ID.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct); // Return 200 OK status
    }

    // --- Delete a product (DELETE /products/{id}) ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by ID", description = "Removes a product from the database using its ID.")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content status
    }
}