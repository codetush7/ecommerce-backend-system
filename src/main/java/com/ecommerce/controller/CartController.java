package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.CartResponseDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;

@RestController
@RequestMapping(value = "/api/cart")
public class CartController {
	@Autowired
	private CartService cartService;

//	 POST /api/cart/add/{productId}
	@PostMapping("/add/{productId}")
	public ResponseEntity<Cart> addToCart(@PathVariable Long productId,@RequestParam int quantity) {
		Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Cart cart = cartService.addToCart(email, productId, quantity);
		
		return ResponseEntity.ok(cart);
	}

//	PUT /api/cart/update/{productId}
	@PutMapping("/update/{productId}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long productId,
                                           @RequestParam int quantity) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Cart cart = cartService.updateCartItem(email, productId, quantity);

        return ResponseEntity.ok(cart);
    }
	
//	DELETE /api/cart/remove/{productId}
	@DeleteMapping("/remove/{productId}")
	public ResponseEntity<Cart> removeFromCart(@PathVariable Long productId) {
		Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        Cart cart = cartService.removeFromCart(email, productId);

        return ResponseEntity.ok(cart);
	}
	
//	 GET /api/cart
	@GetMapping
	public ResponseEntity<CartResponseDTO> getCart() {
		
		Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return ResponseEntity.ok(cartService.getCart(email));

  
	}
}
