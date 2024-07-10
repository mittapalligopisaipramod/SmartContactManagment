package com.main.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
	
//	public JwtService (TokenRepo tokenRepo) {
//		this.tokenRepo = tokenRepo;
//	}
	
	
	private final String secrectKey = "VGhpcyBpcyBhIHZlcnkgc2VjcmV0IGtleSB0aGF0IGlzIHVzZWQgdG8gZ2VuZXJhdGUgSldUIGFuZCBpcyBwcm9wZXJseSBlbmNvZGVk";
	
	
	public String generateToken(String username){
		
		String token= Jwts.builder ()
				.subject (username)
				.claim("roles", Arrays.asList("USER"))
				.issuer ("venkatesh logic tech")
				.issuedAt (new Date (System.currentTimeMillis ()))
				.expiration (new Date (System.currentTimeMillis () + 1000 * 60 * 60 * 10))
				.signWith (getSignKey())
				.compact ();
		return token;
	}
	
	
	
	public SecretKey getSignKey(){
		byte[] keyBytes= Decoders.BASE64URL.decode (secrectKey);
		return Keys.hmacShaKeyFor (keyBytes);
	}
	public Claims getAllClaims(String token){
		return Jwts
				.parser ()
				.verifyWith (getSignKey ())
				.build ()
				.parseSignedClaims(token)
				.getPayload ();
	}
	public <T> T extractClaim(String token, Function<Claims,T> resolver){
		Claims claims=getAllClaims(token);
		return resolver.apply (claims);
		
	}
	public String extractUsername(String token){
		return extractClaim (token, Claims::getSubject);
	}
	
	public boolean isValid(String token, UserDetails user){
		String username=extractUsername (token);
		
		
		return (username.equals (user.getUsername ()) && !isTokenExpired(token)  );
	}
	
	private boolean isTokenExpired (String token) {
		return extractExpiration(token).before(new Date ());
	}
	
	private Date extractExpiration (String token) {
		return extractClaim (token,Claims::getExpiration);
	}
	
	
	
	
	
	
}
