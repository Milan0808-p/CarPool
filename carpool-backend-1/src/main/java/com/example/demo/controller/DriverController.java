package com.example.demo.controller;

import org.springframework.http.MediaType;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.DriverBookingListDTO;
import com.example.demo.dto.driverDtos.DriverProfileDTO;
import com.example.demo.dto.driverDtos.DriverProfileResponseDTO;
import com.example.demo.dto.driverDtos.JourneyRequestDTO;
import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.driverDtos.JourneyUpdateDTO;
import com.example.demo.entity.passengerEntity.PassengerBooking;
import com.example.demo.service.driver.DriverService;
import com.example.demo.service.passenger.PassengerJourneyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/driver")
@RestController
@RequiredArgsConstructor
public class DriverController {

//	@Autowired
	private final DriverService driverService;

//	@Autowired
	private final PassengerJourneyService service;

	
	@PostMapping(value="/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<DriverProfileResponseDTO>> createDriverProfile(
			@Valid @ModelAttribute DriverProfileDTO dto, Authentication auth) {
		
		Long userId = (Long) auth.getPrincipal();
		return driverService.createProfile(dto, userId);

	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<JourneyResponseDTO>> createJourney(@Valid @RequestBody JourneyRequestDTO request,Authentication auth) {
		Long driverId = (Long) auth.getPrincipal();
		return driverService.createJourney(request,driverId);
	}

	@PutMapping("/update/{journeyId}")
	public ResponseEntity<ApiResponse<JourneyResponseDTO>> updateJourney(
			@Valid @RequestBody JourneyUpdateDTO request,
			@PathVariable String journeyId,
			Authentication auth) {
		
		Long userId = (Long) auth.getPrincipal();
		return driverService.updateJourney(request, journeyId, userId);
		
	}
	
	@DeleteMapping("/delete/{journeyId}")
	public ResponseEntity<ApiResponse<Void>> deleteJourney(@PathVariable String journeyId, Authentication auth){
		Long userId = (Long) auth.getPrincipal();
		return driverService.deleteJourney(journeyId,userId);
	}
	
	@GetMapping("/bookings")
	public ResponseEntity<ApiResponse<List<DriverBookingListDTO>>> getBookings( Authentication auth){
		Long userId = (Long) auth.getPrincipal();
		return driverService.getBookings(userId);
	}
	
	@PutMapping("/bookings/{bookingId}/status")
	public ResponseEntity<ApiResponse<DriverBookingListDTO>> updateBookingStatus(
	        @PathVariable String bookingId,
	        @RequestParam String status,
	        Authentication auth) {
		Long userId = (Long) auth.getPrincipal();
	    return driverService.updateBookingStatus(bookingId, userId, status);
	}
}
