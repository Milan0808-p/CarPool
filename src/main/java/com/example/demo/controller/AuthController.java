package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.authDtos.LoginRequest;
import com.example.demo.dto.authDtos.UserDto;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto user) {
		return authService.registerUser(user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		return authService.userLogin(loginRequest);
	}
	
	@PostMapping("/api/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {  //HttpServletRequest :All data coming from client request (header, body,url,etc)

	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(400).body(Map.of("message", "Token missing"));
	    }

	    String token = authHeader.substring(7);

	    return authService.userLogout(token);
	}
}
