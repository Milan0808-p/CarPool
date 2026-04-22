package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.passengerRideDTO.PassengerBookingRequestDTO;
import com.example.demo.dto.passengerRideDTO.PassengerBookingResponseDTO;
import com.example.demo.dto.passengerRideDTO.PassengerRequestDTO;
import com.example.demo.entity.passengerEntity.PassengerBooking;
import com.example.demo.exception.DuplicateBookingException;
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
    public ResponseEntity<ApiResponse<List<JourneyResponseDTO>>> SearchRide(
            @RequestBody PassengerRequestDTO request) {
        return passengerRideService.searchRide(request);
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<PassengerBookingResponseDTO>> bookRide(
            @RequestBody PassengerBookingRequestDTO request) {

       return passengerRideService.bookRide(request);

    }
}