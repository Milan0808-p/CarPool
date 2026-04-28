package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.authDtos.AuthResponseDTO;
import com.example.demo.dto.authDtos.LoginRequestDTO;
import com.example.demo.dto.authDtos.UserDTO;
import com.example.demo.dto.authDtos.UserResponseDTO;
import com.example.demo.exception.TokenMissingException;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(@Valid @RequestBody UserDTO user) {
		return authService.registerUser(user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
		return authService.userLogin(loginRequestDTO);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request) {  //HttpServletRequest :All data coming from client request (header, body,url,etc)

	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        throw new TokenMissingException("Authorization token is missing");
	    }

	    String token = authHeader.substring(7);

	    return authService.userLogout(token);
	}
}
