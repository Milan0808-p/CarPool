package com.example.demo.service;

import java.util.Date;
import java.util.Map;

import com.example.demo.entity.authEntity.BlackListToken;
import com.example.demo.repository.BlackListTokenRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.authDtos.AuthResponseDTO;
import com.example.demo.dto.authDtos.LoginRequestDTO;
import com.example.demo.dto.authDtos.UserDTO;
import com.example.demo.entity.authEntity.RefreshToken;
import com.example.demo.entity.authEntity.Role;
import com.example.demo.entity.authEntity.User;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.security.JwtUtil;


import jakarta.transaction.Transactional;

@Service
@EnableScheduling
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
	@Autowired
	BlackListTokenRepo blacklistRepo;
	
	public ResponseEntity<?> registerUser(UserDTO userDto) {
		if(repo.existsByEmail(userDto.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body("Error: Email is already registered!");
		}
		User user = new User();
		
		user.setRole(Role.valueOf(userDto.getRole().toUpperCase()));
		user.setEmail(userDto.getEmail());
		user.setPhone(userDto.getPhone());
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
	public ResponseEntity<?> userLogin(LoginRequestDTO request) {

		User user = repo.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User is not registered"));

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

		refreshTokenRepo.save(rt); //  FIX

		AuthResponseDTO response = new AuthResponseDTO(
				user.getId(), 
				accessToken, 
				refreshToken, 
				user.getEmail(),
				user.getRole().name() 
				);

		return ResponseEntity.ok(response);
	}

//	public ResponseEntity<?> userLogout(String token) {
//
//	    try {
//	        String email = jwtUtil.extractEmail(token);
//
//	        User user = repo.findByEmail(email)
//	                .orElseThrow(() -> new RuntimeException("User not found"));
//
//	        refreshTokenRepo.deleteByUserId(user.getId());
//
//	        return ResponseEntity.ok(Map.of(
//	                "status", "success",
//	                "message", "Logout successful"
//	        ));
//
//	    } catch (io.jsonwebtoken.ExpiredJwtException e) {
//
//	        //  Still allow logout (important)
//	        return ResponseEntity.ok(Map.of(
//	                "status", "success",
//	                "message", "Token expired but logout successful"
//	        ));
//
//	    } catch (Exception e) {
//
//	        return ResponseEntity.status(500).body(Map.of(
//	                "status", "error",
//	                "message", "Logout failed"
//	        ));
//	    }
//	}


//	public ResponseEntity<?> userLogout(String token) {
//
//		try {
//			String email = jwtUtil.extractEmail(token);
//
//			User user = repo.findByEmail(email)
//					.orElseThrow(() -> new RuntimeException("User not found"));
//
//			//  delete refresh token
//			refreshTokenRepo.deleteByUserId(user.getId());
//
//			// blacklist access token
//			Date expiry = jwtUtil.extractExpiration(token);
//			blacklistRepo.save(new BlacklistedToken(token, expiry));
//
//			return ResponseEntity.ok(Map.of(
//					"status", "success",
//					"message", "Logout successful"
//			));
//
//		} catch (io.jsonwebtoken.ExpiredJwtException e) {
//
//			// even if expired, still blacklist it (optional but clean)
//			Date expiry = e.getClaims().getExpiration();
//			blacklistRepo.save(new BlacklistedToken(token, expiry));
//
//			return ResponseEntity.ok(Map.of(
//					"status", "success",
//					"message", "Token expired but logout successful"
//			));
//
//		} catch (Exception e) {
//
//			return ResponseEntity.status(500).body(Map.of(
//					"status", "error",
//					"message", "Logout failed"
//			));
//		}
//	}





	public ResponseEntity<?> userLogout(String token) {

		try {
			String email = jwtUtil.extractEmail(token);

			User user = repo.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("User not found"));

			// delete refresh token
			refreshTokenRepo.deleteByUserId(user.getId());

			// blacklist access token
			Date expiry = jwtUtil.extractClaim(token, Claims::getExpiration);
			blacklistRepo.save(new BlackListToken(token, expiry));

			return ResponseEntity.ok(Map.of(
					"status", "success",
					"message", "Logout successful"
			));

		} catch (io.jsonwebtoken.ExpiredJwtException e) {

			// still extract expiry from expired token
			Date expiry = e.getClaims().getExpiration();
			blacklistRepo.save(new BlackListToken(token, expiry));

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





	@Scheduled(fixedRate = 60000)
	public void cleanBlacklist() {
		blacklistRepo.deleteByExpiryDateBefore(new Date());
	}
}




