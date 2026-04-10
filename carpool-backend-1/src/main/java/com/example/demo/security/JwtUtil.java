package com.example.demo.security;


import java.util.*;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secretKey;
	private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
//	private String secretKey = "dmkvmekvo8ve56v4dbgfbbfgfh8rrbsegtshry1d5fd5f44v58f4v5d4";

	public String extractEmail(String token) {
		// Claims::getSubject is a method reference
		return extractClaim(token, Claims::getSubject);
	}
	
	public String extractRole(String token) {
	    return extractClaim(token, claims -> claims.get("role", String.class));
	}
	
	// ===================== TOKEN VALIDATION =====================

	// Validates token by checking:
	// 1. Username inside token matches logged-in user
	// 2. Token is not expired
	public Boolean validateToken(String token, UserDetails userDetails) {

		// Extract username from token
		final String username = extractEmail(token);

		// Return true only if username matches AND token is not expired
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// ===================== HELPER METHODS =====================

	// Generic method to extract any claim (username, expiration, etc.)
	// T = type of data you want to extract
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		// Get all claims from token
		final Claims claims = extractAllClaims(token);

		// Apply the function (example: getSubject, getExpiration)
		return claimsResolver.apply(claims);
	}

	// Extracts ALL claims (payload) from the JWT
	// This method also verifies the token signature using secretKey
	private Claims extractAllClaims(String token) {
//	    Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}

	// Checks whether the token has expired
	public  Boolean isTokenExpired(String token) {

		// Get expiration date from token and compare with current time
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
	
	public String createAccessToken(String email, String role) {
	    return Jwts.builder()
	            .setSubject(email)
	            .claim("role", role) 
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 min
	            .signWith(key, SignatureAlgorithm.HS256)  //Creates a secure signature for the JWT token (HMAC + SHA-256)
	            .compact();
	}

	public String createRefreshToken(String email) {
	    return Jwts.builder()
	            .setSubject(email)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
	            .signWith(key, SignatureAlgorithm.HS256)
	            .compact();
	}

}
