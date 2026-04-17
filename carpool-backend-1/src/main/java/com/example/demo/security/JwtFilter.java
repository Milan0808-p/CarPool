package com.example.demo.security;


import java.io.IOException;
import java.util.*;

import com.example.demo.repository.BlackListTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.CustomUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	CustomUserDetailService userDetailsService;
	@Autowired
	BlackListTokenRepo blacklistRepo;

	@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/login") || path.startsWith("/register") || path.startsWith("/refresh") || path.startsWith("/verify-otp") || path.startsWith("/reset-password") || path.startsWith("/send-otp");
    }
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		    String token = authHeader.substring(7);

		if (blacklistRepo.existsByToken(token)) {
			System.out.println("Token is blacklisted");

			//  Just continue without setting auth
			filterChain.doFilter(request, response);
			return;
		}



		try {
		    String email = jwtUtil.extractEmail(token);

		    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

		        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		        
		        // this is validate user using token and userDetails
		        if (jwtUtil.validateToken(token, userDetails)) {
		        	
		        	// Create a logged-in user object for Spring Security
		            UsernamePasswordAuthenticationToken authToken =
		                    new UsernamePasswordAuthenticationToken(
		                            email,
		                            null,
		                            userDetails.getAuthorities()
		                    );
		            
		            // Attach request-related info (like IP, session) to the logged-in user
		            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		            
		            // Set the current user as logged in for this request
		            
		            SecurityContextHolder.getContext().setAuthentication(authToken);
		        }
		    }

		} catch (Exception e) {
		    // VERY IMPORTANT
		    // Don't break the request, just continue
		    System.out.println("JWT Error: " + e.getMessage());
		}

		filterChain.doFilter(request, response);
	}

}
