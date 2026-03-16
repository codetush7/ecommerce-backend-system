package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.ChangePasswordDTO;
import com.ecommerce.dto.UpdateUserDTO;
import com.ecommerce.dto.UserResponseDTO;
import com.ecommerce.entity.User;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
	@Autowired
	private UserService userService;
	
//	POST /api/users/register	
	@PostMapping("/register")
	public User registerUser(@RequestBody User user) {
		return userService.userRegistration(user);
	}

	
//	 GET /api/users/{id}
	@GetMapping("/{id}")
	public UserResponseDTO getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
//	 PUT /api/users/{id}
	@PutMapping("/{id}")
	public ResponseEntity<UserResponseDTO> updateUser(
	        @PathVariable Long id,
	        @RequestBody UpdateUserDTO dto) {

	    return ResponseEntity.ok(userService.updateUser(id, dto));
	}
	
//	 DELETE /api/users/{id} (Admin only)
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}
//	ChangePassword
	@PutMapping("/change-password")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO dto){
		String message = userService.changePassword(dto);
		return ResponseEntity.ok(message);
	}
}
