package com.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCart(Cart cart);
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
