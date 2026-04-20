package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.passengerRideDTO.PassengerRequestDTO;
import com.example.demo.dto.passengerRideDTO.PassengerResponseDTO;
import com.example.demo.service.passenger.PassengerRideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/passenger")
public class PassengerRideController {

    @Autowired
    PassengerRideService passengerRideService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<JourneyResponseDTO>>> bookRide(
            @RequestBody PassengerRequestDTO request) {
        System.out.println("In bookRIde Controller ");
        List<JourneyResponseDTO> rides = passengerRideService.bookRide(request);
        System.out.println("Rides: " + rides);
        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Matched rides found", rides)
        );
    }
}