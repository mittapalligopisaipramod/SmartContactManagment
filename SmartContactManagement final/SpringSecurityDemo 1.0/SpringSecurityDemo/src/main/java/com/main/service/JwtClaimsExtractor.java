package com.main.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtClaimsExtractor {
	
	public String extractUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			System.out.println("Authentication is null");
			return null;
		}
		
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof Jwt)) {
			System.out.println("Principal is not of type Jwt");
			return null;
		}
		
		Jwt jwt = (Jwt) principal;
		String username = jwt.getClaim("sub");
		if (username == null) {
			System.out.println("JWT 'sub' claim is null");
		}
		return username;
	}
}
