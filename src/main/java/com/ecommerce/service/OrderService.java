package com.ecommerce.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.enums.PaymentMode;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;

@Service
public class OrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderitemRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserRepository userRepository;
	
	//Order Checkout
	@Transactional
	public Order checkout(String email,PaymentMode paymentMode) {
		LOGGER.info("Checkout started for user");
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		List<CartItem> cartItems =  cartItemRepository.findByCart(cart);
		
		if(cartItems.isEmpty()) {
			throw new IllegalArgumentException("Cart is empty");
		}
		
		Order order = new Order();
		order.setUser(user);
		order.setDate(LocalDate.now());
		order.setOrderStatus(OrderStatus.PLACED);
		order.setPaymentStatus("PENDING");
		order.setPaymentMode(paymentMode);
		
		double totalAmount = 0;
		
		order = orderRepository.save(order);
		
		for(CartItem item : cartItems) {
			
			Product product = item.getProduct();
			if(product.getStock() < item.getQuantity()) {
				throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
			}
			
			//update the stock
			product.setStock(product.getStock() - item.getQuantity());
			
			productRepository.save(product);
			
			OrderItem orderItem = new OrderItem();
			
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(item.getQuantity());
			orderItem.setPrice(product.getPrice());
			
			orderitemRepository.save(orderItem);
			
			totalAmount += product.getPrice()*item.getQuantity();
			
		}
		order.setTotal_amount(totalAmount);
		orderRepository.save(order);
		
		cartItemRepository.deleteAll(cartItems);
		
		cart.setTotalPrice(0.0);
		cartRepository.save(cart);
		LOGGER.info("Order created with id {}", order.getId());
		return order;
		
	}
	public Order getOrder(Long id) {
		
		return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
	}
	
	public List<Order> getOrderByUser(String email){
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found "));
		return orderRepository.findByUser(user);
	}
	
	public Order updateOrderStatus(Long orderId, OrderStatus status) {
		LOGGER.info("Updating order {} status to {}", orderId, status);
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
		order.setOrderStatus(status);
		
		return orderRepository.save(order);		
	}
	
	public Order processPayment(Long orderId, boolean paymentSuccess) {
		
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
		
		if(paymentSuccess) {
			order.setPaymentStatus("SUCCESS");
			order.setOrderStatus(OrderStatus.PLACED);
		}
		else {
			order.setPaymentStatus("FAILED");
			order.setOrderStatus(OrderStatus.CANCELLED);
		}
		return orderRepository.save(order);
	}
	
	
}
