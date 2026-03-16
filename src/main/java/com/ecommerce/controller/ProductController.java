package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.PageResponseDTO;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.ProductCategory;
import com.ecommerce.service.ProductService;

@RestController
@RequestMapping(value = "/api")
public class ProductController {

	@Autowired
	private ProductService productService;

	// Add Product
	// POST /api/products (Admin)
	@PostMapping("/products")
	public Product addProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}

	// Get all products
	// GET /api/products
	@GetMapping("/products")
	public ResponseEntity<PageResponseDTO<Product>> getAllProducts(Pageable pageable) {
		PageResponseDTO<Product> products = productService.getAllProducts(pageable);

	    return ResponseEntity.ok(products);
	}

	// Get product by id
	// GET /api/products/{id}
	@GetMapping("/products/{id}")
	public Product getProductById(@PathVariable Long id) {
		return productService.getProductById(id);
	}

	// Update the products
	// PUT /api/products/{id} (Admin)
	@PutMapping("/products/{id}")
	public Product updateProductById(@PathVariable Long id, @RequestBody Product product) {
		return productService.updateProduct(id, product);
	} 

	// Delete Product
	// DELETE /api/users/{id}
	@DeleteMapping("/products/{id}")
	public void deleteProductById(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
	
	//Category Filter
	@GetMapping("/products/category/{category}")
	public ResponseEntity<PageResponseDTO<Product>> getProductsByCategory(
	        @PathVariable ProductCategory category,
	        Pageable pageable) {

	    return ResponseEntity.ok(productService.getProductsByCategory(category, pageable));
	}
	
	@GetMapping("/products/price")
	public ResponseEntity<PageResponseDTO<Product>> getProductsByPriceRange(
	        @RequestParam Double minPrice,
	        @RequestParam Double maxPrice,
	        Pageable pageable) {

	    return ResponseEntity.ok(
	            productService.getProductsByPriceRange(minPrice, maxPrice, pageable)
	    );
	}

}
