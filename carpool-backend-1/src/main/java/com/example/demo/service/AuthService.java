package com.example.demo.service;

import java.util.Date;
import java.util.Map;

import com.example.demo.entity.authEntity.BlackListToken;
import com.example.demo.repository.BlackListTokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import com.cloudinary.api.ApiResponse;
import com.example.demo.dto.authDtos.AuthResponseDTO;
import com.example.demo.dto.authDtos.LoginRequestDTO;
import com.example.demo.dto.authDtos.UserDTO;
import com.example.demo.dto.authDtos.UserResponseDTO;
import com.example.demo.entity.authEntity.RefreshToken;
import com.example.demo.entity.authEntity.Role;
import com.example.demo.entity.authEntity.User;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.security.JwtUtil;
import com.example.demo.dto.ApiResponse;
import jakarta.transaction.Transactional;

@Service
@EnableScheduling
public class AuthService {

	@Autowired
	private AuthRepository repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private RefreshTokenRepo refreshTokenRepo;
	
	@Autowired
	private BlackListTokenRepo blacklistRepo;
	
	public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(UserDTO userDto) {
		
		if (repo.existsByEmail(userDto.getEmail())) {
		    throw new ResourceAlreadyExistsException("User is already registered!");
		}
		
//		User user = new User();
//		user.setRole(Role.valueOf(userDto.getRole().toUpperCase()));
//		user.setEmail(userDto.getEmail());
//		user.setPhone(userDto.getPhone());
//		user.setUsername(userDto.getUsername());
//		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		User user = User.builder()
				.role(Role.valueOf(userDto.getRole().toUpperCase()))
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.build();
		
		if (user.getRole() == null) {
			user.setRole(Role.USER);
		}

		repo.save(user);

		UserResponseDTO data = UserResponseDTO.builder()
				.username(user.getUsername())
				.role(user.getRole().name())
				.phone(user.getPhone())
				.email(user.getEmail())
				.build();
	
		return ResponseEntity.ok(
	            new ApiResponse<>("success", "Register successful", data)
	    );
		
	}

	
	public ResponseEntity<ApiResponse<AuthResponseDTO>> userLogin(LoginRequestDTO request) {

		User user = repo.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		 if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			 throw new InvalidCredentialsException("Invalid credentials");
		 }

		String accessToken = jwtUtil.createAccessToken(user.getId(), user.getRole().name());
		String refreshToken = jwtUtil.createRefreshToken(user.getId());

		// delete old refresh token (or use device-based later)
		refreshTokenRepo.deleteByUserId(user.getId());

//		RefreshToken rt = new RefreshToken();
//		rt.setToken(refreshToken);
//		rt.setUser(user);
//		rt.setExpiryDate(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
		
		RefreshToken rt = RefreshToken.builder()
				.token(refreshToken)
				.user(user)
				.expiryDate(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
				.build();
				
		refreshTokenRepo.save(rt);

		AuthResponseDTO data = AuthResponseDTO.builder()
		        .userId(user.getPublicId())
		        .accessToken(accessToken)
		        .refreshToken(refreshToken)
		        .email(user.getEmail())
		        .role(user.getRole().name())
		        .build();

	    return ResponseEntity.ok(
	            new ApiResponse<>("success", "Login successful", data)
	    );
	}

	public ResponseEntity<ApiResponse<Object>> userLogout(String token) {

		if (!jwtUtil.validateToken(token)) {
			throw new InvalidTokenException("Invalid token");
		}

		Long userId = jwtUtil.extractUserId(token);
		String jti = jwtUtil.extractJti(token);
		Date expiry = jwtUtil.extractClaim(token, Claims::getExpiration);

		refreshTokenRepo.deleteByUserId(userId);
		blacklistRepo.save(new BlackListToken(jti, expiry));

		return ResponseEntity.ok(
				new ApiResponse<>("success", "Logout successful", null));
	}
	
	@Scheduled(fixedDelay = 1000000)
	@Transactional
	public void cleanBlacklist() {
	    blacklistRepo.deleteByExpiryDateBefore(new Date());
	}
}
