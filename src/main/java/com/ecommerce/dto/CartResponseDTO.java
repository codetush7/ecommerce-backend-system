package com.ecommerce.dto;

import java.util.List;

public class CartResponseDTO {
	private Long cartId;
    private Double totalPrice;
    private List<CartItemResponseDTO> items;
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public List<CartItemResponseDTO> getItems() {
		return items;
	}
	public void setItems(List<CartItemResponseDTO> items) {
		this.items = items;
	}
    

}
