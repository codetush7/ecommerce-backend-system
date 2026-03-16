package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Order;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.enums.PaymentMode;
import com.ecommerce.service.OrderService;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;

//	 POST /api/orders/checkout
	@PostMapping("/checkout")
	public ResponseEntity<Order> checkoutOrder(@RequestParam PaymentMode paymentMode) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		Order order = orderService.checkout(email, paymentMode);

		return ResponseEntity.ok(order);
	}

//	 GET /api/orders
	@GetMapping
	public ResponseEntity<List<Order>> getOrdersByUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		List<Order> orders = orderService.getOrderByUser(email);

		return ResponseEntity.ok(orders);
	}

//	 GET /api/orders/{id}
	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrder(@PathVariable Long id) {

		Order order = orderService.getOrder(id);

		return ResponseEntity.ok(order);
	}

//	PUT /api/orders/{id}/status (Admin)
	@PutMapping("/{id}/status")
	public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {

		Order order = orderService.updateOrderStatus(id, status);

		return ResponseEntity.ok(order);
	}

//	POST /api/orders/{id}/payment
	@PostMapping("/{id}/payment")
	public ResponseEntity<Order> processPayment(@PathVariable Long id, @RequestParam boolean success) {

		Order order = orderService.processPayment(id, success);

		return ResponseEntity.ok(order);
	}

}
