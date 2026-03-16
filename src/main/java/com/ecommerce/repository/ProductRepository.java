package com.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Product;
import com.ecommerce.enums.ProductCategory;

public interface ProductRepository extends JpaRepository<Product, Long>{
	Page<Product> findByCategory(ProductCategory category, Pageable pageable);
	Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
}
