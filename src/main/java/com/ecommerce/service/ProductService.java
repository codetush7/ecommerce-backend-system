package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import com.ecommerce.dto.PageResponseDTO;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.ProductCategory;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.ProductRepository;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	// Add Product

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	// Get All Products
	public PageResponseDTO<Product> getAllProducts(Pageable pageable) {
		LOGGER.info("Fetching all products with pagination");

		Page<Product> page = productRepository.findAll(pageable);

		return new PageResponseDTO<Product>(page.getContent(), page.getNumber(), page.getSize(),
				page.getTotalElements(), page.getTotalPages(), page.isLast());
	}

	// Get Product By id
	public Product getProductById(Long id) {
		LOGGER.info("Fetching product with id: {}", id);
		return productRepository.findById(id).orElseThrow(() -> {
			LOGGER.error("Product not found with id: {}", id);
			return new ResourceNotFoundException("Product not found with id: " + id);
		});

	}

	// Update Product By id
	public Product updateProduct(Long id, Product updatedProduct) {
		LOGGER.info("Updating product with id: {}", id);
		Product existingProduct = productRepository.findById(id).orElseThrow(() -> {
			LOGGER.error("Product not found for update with id: {}", id);
			return new ResourceNotFoundException("Product not found with id: " + id);
		});
		existingProduct.setName(updatedProduct.getName());
		existingProduct.setDescription(updatedProduct.getDescription());
		existingProduct.setCategory(updatedProduct.getCategory());
		existingProduct.setPrice(updatedProduct.getPrice());
		existingProduct.setRating(updatedProduct.getRating());
		existingProduct.setStock(updatedProduct.getStock());
		existingProduct.setImageUrl(updatedProduct.getImageUrl());

		return productRepository.save(existingProduct);

	}

	// Delete Product by id
	public void deleteProduct(Long id) {

		LOGGER.info("Deleting product with id: {}", id);
		Product product = productRepository.findById(id).orElseThrow(() -> {
			LOGGER.error("Product not found for deletion with id: {}", id);
			return new ResourceNotFoundException("Product not found with id: " + id);
		});

		productRepository.delete(product);
		LOGGER.info("Product deleted successfully with id: {}", id);
	}

	public PageResponseDTO<Product> getProductsByCategory(ProductCategory category, Pageable pageable) {
		LOGGER.info("Fetching products for category: {}", category);
		Page<Product> page = productRepository.findByCategory(category, pageable);

		return new PageResponseDTO<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
				page.getTotalPages(), page.isLast());
	}

	public PageResponseDTO<Product> getProductsByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
		LOGGER.info("Fetching products between price {} and {}", minPrice, maxPrice);
		Page<Product> page = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);

		return new PageResponseDTO<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
				page.getTotalPages(), page.isLast());
	}
}
