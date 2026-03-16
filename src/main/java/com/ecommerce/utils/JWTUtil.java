package com.ecommerce.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	private static final String SECRET_KEY = "your-secure-secret-key-min-32bytes";
	private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	
	//Generate Token
	
	public String generateToken(String email,String role) {
		return Jwts.builder()
					.subject(email)
					.claim("role",role)
					.issuedAt(new Date())
					.expiration(new Date(System.currentTimeMillis()+1000*60*60))
					.signWith(key)
					.compact();
	}
	
	// Extract Email
	public String extractEmail(String token) {
		
		Claims claims = Jwts.parser()
							.verifyWith(key)
							.build()
							.parseSignedClaims(token)
							.getPayload();
		return claims.getSubject();
	}
	
	//Extract Role
	public String extractRole(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.get("role",String.class);
	}
}
