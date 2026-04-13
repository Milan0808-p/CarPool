package com.example.demo.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.authDtos.AuthResponse;
import com.example.demo.dto.authDtos.LoginRequest;
import com.example.demo.dto.authDtos.UserDto;
import com.example.demo.entity.authEntity.RefreshToken;
import com.example.demo.entity.authEntity.Role;
import com.example.demo.entity.authEntity.User;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.security.JwtUtil;


import jakarta.transaction.Transactional;

@Service
public class AuthService {
	
	User user;
	
	
	@Autowired
	AuthRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	RefreshTokenRepo refreshTokenRepo;
	
	public ResponseEntity<?> registerUser(UserDto userDto) {
		if(repo.existsByEmail(userDto.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body("Error: Email is already taken!");
		}
		User user = new User();
		
		user.setRole(Role.valueOf(userDto.getRole().toUpperCase()));
		user.setEmail(userDto.getEmail());
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
	
		if (user.getRole() == null) {
			user.setRole(Role.USER);
		}

		repo.save(user);
		
		return ResponseEntity.ok(Map.of(
			    "message", "User registered successfully"
			));
	}
	
	@Transactional
	public ResponseEntity<?> userLogin(LoginRequest request) {

		User user = repo.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid password");
		}

		String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole().name());
		String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

		// delete old token
		refreshTokenRepo.deleteByUserId(user.getId());
		refreshTokenRepo.flush();

		RefreshToken rt = new RefreshToken();
		rt.setToken(refreshToken);
		rt.setUser(user);
		rt.setExpiryDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));

		refreshTokenRepo.save(rt); // ✅ FIX

		AuthResponse response = new AuthResponse(
				user.getId(), 
				accessToken, 
				refreshToken, 
				user.getEmail(),
				user.getRole().name() 
				);

		return ResponseEntity.ok(response);
	}

	public ResponseEntity<?> userLogout(String token) {

	    try {
	        String email = jwtUtil.extractEmail(token);

	        User user = repo.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        refreshTokenRepo.deleteByUserId(user.getId());

	        return ResponseEntity.ok(Map.of(
	                "status", "success",
	                "message", "Logout successful"
	        ));

	    } catch (io.jsonwebtoken.ExpiredJwtException e) {

	        //  Still allow logout (important)
	        return ResponseEntity.ok(Map.of(
	                "status", "success",
	                "message", "Token expired but logout successful"
	        ));

	    } catch (Exception e) {

	        return ResponseEntity.status(500).body(Map.of(
	                "status", "error",
	                "message", "Logout failed"
	        ));
	    }
	}
	
}
