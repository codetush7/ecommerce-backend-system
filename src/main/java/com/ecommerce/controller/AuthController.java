package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.User;
import com.ecommerce.service.UserService;
import com.ecommerce.utils.JWTUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    
	
	@Autowired
	private UserService userService;
	@Autowired
	private JWTUtil jwtUtil;
	
	
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password) {
		User user = userService.loginUser(email, password);
		
		return jwtUtil.generateToken(user.getEmail(),user.getRole().name());
	}
}
