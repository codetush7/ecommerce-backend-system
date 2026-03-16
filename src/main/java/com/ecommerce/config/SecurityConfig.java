package com.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.security.JWTAuthFilter;

@Configuration
public class SecurityConfig {
	@Autowired
	private JWTAuthFilter jwtAuthFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				// public APIs
				.requestMatchers(
				        "/v3/api-docs/**",
				        "/swagger-ui/**",
				        "/swagger-ui.html",
				        "/api/users/register",
				        "/api/users/login"
				).permitAll()
				.requestMatchers("/api/auth/**").permitAll()
				
				.requestMatchers("/api/users/register").permitAll()
				.requestMatchers("/api/users/login").permitAll()
				
				 // change password (logged in users only)
		        .requestMatchers(HttpMethod.PUT, "/api/users/change-password")
		        .hasAnyRole("ADMIN","CUSTOMER")
  
				// ADMIN Only APIs 
				.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

				.requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

				.requestMatchers(HttpMethod.PUT, "/api/orders/*/status").hasRole("ADMIN")

				// CUSTOMER APIs
				.requestMatchers("/api/cart/**").hasRole("CUSTOMER")
				.requestMatchers(HttpMethod.POST, "/api/orders/checkout").hasRole("CUSTOMER")
				.requestMatchers(HttpMethod.GET, "/api/orders/**").hasRole("CUSTOMER")

				// Both
				.requestMatchers(HttpMethod.GET, "/api/products/**")
				.hasAnyRole("ADMIN", "CUSTOMER")
				
				.requestMatchers(HttpMethod.PUT, "/api/users/**")
				.hasAnyRole("ADMIN","CUSTOMER")
				
				.requestMatchers(HttpMethod.GET, "/api/users/**")
				.hasAnyRole("ADMIN","CUSTOMER")

				.anyRequest().authenticated()

		)

				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
