package com.ecommerce.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.ChangePasswordDTO;
import com.ecommerce.dto.UpdateUserDTO;
import com.ecommerce.dto.UserResponseDTO;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.UserRepository;

@Service
public class UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;

//	registerUser()
	public User userRegistration(User user) {
		LOGGER.info("Registering new user with email {}", user.getEmail());
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User savedUser = userRepository.save(user);

	    // Send welcome email after successful save
		try {
		    emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getName());
		} catch (Exception e) {
		    LOGGER.error("Failed to send welcome email to {}", savedUser.getEmail());
		}

	    return savedUser;

	}
//	loginUser()

	public User loginUser(String email, String password) {
		LOGGER.info("User login attempt for email {}", email);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("Invalid password");
		}
		return user;
	}
//	getUserById()

	public UserResponseDTO getUserById(long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User loggedInUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Logged in user not found"));
		User requestedUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		// IF user is not admin and trying to access another user

		if (!loggedInUser.getRole().name().equals("ADMIN") && !loggedInUser.getId().equals(requestedUser.getId())) {
			throw new SecurityException("Access denied");
		}
		return modelMapper.map(requestedUser, UserResponseDTO.class);

	}

//	updateUser()
	public UserResponseDTO updateUser(Long id, UpdateUserDTO dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		
		User loggedInUser = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Logged in user Not Found"));
		
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		//Permission Check
		if(!loggedInUser.getRole().name().equals("ADMIN") && !loggedInUser.getId().equals(existingUser.getId())) {
			throw new SecurityException("Acces Denied");
		}
		
		if(dto.getName() != null) {
			existingUser.setName(dto.getName());
		}
		
		if(dto.getEmail() != null) {
			if(userRepository.findByEmail(dto.getEmail()).isPresent()){
	            throw new IllegalArgumentException("Email already exists");
	        }

			existingUser.setEmail(dto.getEmail());
			
		}
		userRepository.save(existingUser);
		
		return modelMapper.map(existingUser, UserResponseDTO.class);
	}

//	deleteUser()
	public void deleteUser(Long userID) {
		LOGGER.info("Deleting user with id {}", userID);
		if (!userRepository.existsById(userID)) {
			throw new ResourceNotFoundException("User not found with id: " + userID);
		}
		userRepository.deleteById(userID);
	}
	

	// Change Password

	public String changePassword(ChangePasswordDTO dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Old password is incorrect");
		}
		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		userRepository.save(user);

		return "Password Updated Successfully";

	}
}
