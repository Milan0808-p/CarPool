package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.example.demo.entity.passangerEntity.PassengerBooking;
import com.example.demo.service.driver.DriverService;
//import com.example.demo.service.driver.JourneyService;
import com.example.demo.service.passenger.PassengerJourneyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/driver")
@RestController
@RequiredArgsConstructor
public class DriverController {

	@Autowired
	private final DriverService driverService;
	
//	@Autowired
//	private final JourneyService journeyService;
	
	@Autowired
    private final PassengerJourneyService service;
    
	@PostMapping("/profile")
	public ResponseEntity<ApiResponse<DriverProfileResponseDTO>> createDriverProfile(
			@Valid @ModelAttribute DriverProfileDTO dto, @RequestHeader Long userId) {

		return driverService.createProfile(dto, userId);

	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<JourneyResponseDTO>> createJourney(@Valid @RequestBody JourneyRequestDTO request,@RequestHeader Long driverId) {
		return driverService.createJourney(request,driverId);
	}

	@PutMapping("/update/{journeyId}")
	public ResponseEntity<ApiResponse<JourneyResponseDTO>> updateJourney(
			@Valid @RequestBody JourneyUpdateDTO request,
			@PathVariable Long journeyId,
			@RequestHeader(value = "driverId", required = false) Long driverId) {
		
		return driverService.updateJourney(request, journeyId, driverId);
		
	}
	
	@DeleteMapping("/delete/{journeyId}")
	public ResponseEntity<ApiResponse<Void>> deleteJourney(@PathVariable Long journeyId, @RequestHeader(value = "driverId", required = false) Long driverId){
		return driverService.deleteJourney(journeyId,driverId);
	}
	
	@GetMapping("/bookings")
	public ResponseEntity<ApiResponse<List<PassengerBooking>>> getBookings( @RequestHeader(value = "driverId", required = false) Long driverId){
		
		return driverService.getBookings(driverId);
	}
	
}
