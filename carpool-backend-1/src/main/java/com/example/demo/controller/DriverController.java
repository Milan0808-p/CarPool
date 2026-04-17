package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.DriverProfileDTO;
import com.example.demo.dto.driverDtos.DriverProfileResponseDTO;
import com.example.demo.dto.driverDtos.JourneyRequestDTO;
import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.driverDtos.JourneyUpdateDTO;
import com.example.demo.service.driver.DriverService;
import com.example.demo.service.driver.JourneyService;
import com.example.demo.service.passenger.PassengerJourneyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/driver")
@RestController
@RequiredArgsConstructor
public class DriverController {

	@Autowired
	private final DriverService driverService;
	
	@Autowired
	private final JourneyService journeyService;

	@Autowired
    private final PassengerJourneyService service;
    
	@PostMapping("/profile")
	public ResponseEntity<ApiResponse<DriverProfileResponseDTO>> createDriverProfile(
			@Valid @ModelAttribute DriverProfileDTO dto, @RequestHeader Long userId) {

		return driverService.createProfile(dto, userId);

	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<JourneyResponseDTO>> createJourney(@RequestBody JourneyRequestDTO request) {
		return journeyService.createJourney(request);
	}

	@PutMapping("/update/{journeyId}")
	public ResponseEntity<ApiResponse<JourneyResponseDTO>> updateJourney(@RequestBody JourneyUpdateDTO request,
			@PathVariable Long journeyId) {

		return journeyService.updateJourney(request, journeyId);
	}
	
}
