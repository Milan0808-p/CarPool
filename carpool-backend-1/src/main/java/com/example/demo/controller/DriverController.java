package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.DriverProfileDTO;
import com.example.demo.dto.driverDtos.DriverProfileResponseDTO;
import com.example.demo.service.driver.DriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/driver")
@RestController
@RequiredArgsConstructor
public class DriverController {
	
	@Autowired
	DriverService driverService;
	
	@PostMapping("/profile")
	public ResponseEntity<ApiResponse<DriverProfileResponseDTO>> createDriverProfile(
	        @Valid @ModelAttribute DriverProfileDTO dto,
	        @RequestHeader Long userId) {
		
	    return driverService.createProfile(dto,userId);
	    
	}
}
