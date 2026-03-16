package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	public void sendWelcomeEmail(String toEmail, String name) {

	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setTo(toEmail);
	    message.setSubject("Welcome to Our E-Commerce Platform");

	    message.setText(
	        "Hello " + name + ",\n\n" +
	        "Welcome to our E-Commerce platform.\n" +
	        "Your account has been created successfully.\n\n" +
	        "Happy Shopping!"
	    );

	    mailSender.send(message);
	}
	public void sendOrderConfirmation(String toEmail, Long orderId) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Order Confirmation");
        message.setText("Your order with ID " + orderId + " has been placed successfully.");

        mailSender.send(message);
    }
}
