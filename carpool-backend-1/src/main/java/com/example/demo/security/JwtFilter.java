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
		    
		try {
			
			if (token.split("\\.").length != 3) {
	            sendError(response, "Invalid JWT format");
	            return;
	        }
			
			String jti = jwtUtil.extractJti(token);
		    
			if (blacklistRepo.existsByToken(jti)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Token is blacklisted");
	            return;
			}
			
		    Long userId = jwtUtil.extractUserId(token);

		    if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

		        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(userId));
		        
		        // this is validate user using token and userDetails
		        if (jwtUtil.validateToken(token, userDetails)) {
		        	
		        	// Create a logged-in user object for Spring Security
		            UsernamePasswordAuthenticationToken authToken =
		                    new UsernamePasswordAuthenticationToken(
		                    		userId,
		                            null,
		                            userDetails.getAuthorities()
		                    );
		            
		            // Attach request-related info (like IP, session) to the logged-in user
		            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		            
		            // Set the current user as logged in for this request
		            
		            SecurityContextHolder.getContext().setAuthentication(authToken);
		        }
		    }

		} catch (io.jsonwebtoken.MalformedJwtException e) {
		    sendError(response, "Invalid token format");
		    return;
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
		    sendError(response, "Token expired");
		    return;
		} catch (Exception e) {
		    sendError(response, "Invalid token");
		    return;
		}

		filterChain.doFilter(request, response);
	}
	
	
	private void sendError(HttpServletResponse response, String message) throws IOException {
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    response.setContentType("application/json");
	    response.getWriter().write(
	        "{\"status\":\"error\",\"message\":\"" + message + "\",\"data\":null}"
	    );
	}
	
}
