package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.driverDtos.DriverProfileDTO;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/driver")
@RestController
@RequiredArgsConstructor
public class DriverController {
	
	@PostMapping("/driver/profile")
	public ResponseEntity<?> createDriverProfile(
	        @ModelAttribute DriverProfileDTO dto) {

	    return driverService.createProfile(dto);
	}
	
}
