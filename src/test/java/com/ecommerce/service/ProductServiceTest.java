package com.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ecommerce.dto.PageResponseDTO;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.ProductCategory;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.ProductRepository;

class ProductServiceTest {
	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product product;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		product = new Product(1L, "Laptop", "Gaming Laptop", 50000.0, 10, ProductCategory.ELECTRONICS, "image.jpg",
				4.5);
	}

	@Test
	void testSaveProduct() {
		when(productRepository.save(product)).thenReturn(product);

		Product savedProduct = productService.saveProduct(product);

		assertNotNull(savedProduct);

		assertEquals("Laptop", savedProduct.getName());

		verify(productRepository, times(1)).save(product);
	}

	@Test
	void testGetProductById() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		Product result = productService.getProductById(1L);

		assertEquals("Laptop", result.getName());
	}

	@Test
	void testGetProductById_NotFound() {

		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
	}

	@Test
	void testGetAllProducts() {

		PageRequest pageable = PageRequest.of(0, 5);

		Page<Product> page = new PageImpl<>(List.of(product));

		when(productRepository.findAll(pageable)).thenReturn(page);

		PageResponseDTO<Product> response = productService.getAllProducts(pageable);

		assertEquals(1, response.getContent().size());
	}
	@Test
	void testUpdateProduct() {

	    Product updatedProduct = new Product(
	            null,
	            "Updated Laptop",
	            "New Description",
	            60000.0,
	            20,
	            ProductCategory.ELECTRONICS,
	            "newimage.jpg",
	            4.8
	    );

	    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
	    when(productRepository.save(product)).thenReturn(product);

	    Product result = productService.updateProduct(1L, updatedProduct);

	    assertEquals("Updated Laptop", result.getName());
	    assertEquals(60000.0, result.getPrice());
	}
	
	@Test
	void testDeleteProduct() {

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		productService.deleteProduct(1L);

		verify(productRepository, times(1)).delete(product);
	}
}
