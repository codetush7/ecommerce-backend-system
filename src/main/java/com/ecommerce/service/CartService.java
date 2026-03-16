package com.ecommerce.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.CartItemResponseDTO;
import com.ecommerce.dto.CartResponseDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;
	

	// Add Product to Cart
	@Transactional
	public Cart addToCart(String email, Long productId, int quantity) {
		LOGGER.info("Adding product {} to cart for user {}", productId, email);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found with id: "));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found with id: " + productId));

		if (product.getStock() < quantity)
			throw new IllegalArgumentException("Product is Out of Stock");

		Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			newCart.setTotalPrice(0.0);
			return cartRepository.save(newCart);
		});
		CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

		if (cartItem != null) {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		} else {
			cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cart.getCartItems().add(cartItem); 
		}

		cartItemRepository.save(cartItem);

		double totalPrice = cart.getTotalPrice() + (product.getPrice() * quantity);
		cart.setTotalPrice(totalPrice);

		cartRepository.save(cart);
		return cart;
	}

	// Update Cart item

	public Cart updateCartItem(String email, Long productId, int quantity) {
		LOGGER.info("Updating cart item for product {}", productId);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: "));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("PRoduct Not found with id: " + productId));
		CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product is not in the cart"));

		cartItem.setQuantity(quantity);
		cartItemRepository.save(cartItem);
		updateCartTotal(cart);
		return cart;
	}

	// Completely Remove from cart

	public Cart removeFromCart(String email, Long productId) {
		LOGGER.info("Removing product {} from cart for user {}", productId);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " ));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
		CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Product is not in the cart"));

		cartItemRepository.delete(cartItem);
		updateCartTotal(cart);
		return cart;

	}

	// Get all products in the cart

	public CartResponseDTO getCart(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: "));

		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		List<CartItemResponseDTO> items = cart.getCartItems()
	            .stream()
	            .map(item -> {
	                CartItemResponseDTO dto = new CartItemResponseDTO();
	                dto.setProductId(item.getProduct().getId());
	                dto.setProductName(item.getProduct().getName());
	                dto.setPrice(item.getProduct().getPrice());
	                dto.setQuantity(item.getQuantity());
	                return dto;
	            })
	            .toList();
		CartResponseDTO response = new CartResponseDTO();
	    response.setCartId(cart.getId());
	    response.setTotalPrice(cart.getTotalPrice());
	    response.setItems(items);

	    return response;
	}

	// Helper method to update the price
	private void updateCartTotal(Cart cart) {

		double total = 0;

		for (CartItem item : cartItemRepository.findByCart(cart)) {
			total += item.getProduct().getPrice() * item.getQuantity();
		}

		cart.setTotalPrice(total);
		cartRepository.save(cart);
	}
}
