package com.example.demo.controller;
import com.example.demo.dto.driverDtos.JourneyRequestDTO;
import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.driverDtos.JourneyUpdateDTO;
import com.example.demo.service.driver.JourneyService;
import com.example.demo.service.passenger.PassengerJourneyService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journies")
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService journeyService;

    private final PassengerJourneyService service;

    // 🚀 POST JOURNEY
    @PostMapping("/create")
    public JourneyRequestDTO createJourney(@RequestBody JourneyRequestDTO request) {
        return journeyService.createJourney(request);
    }
    
    @PutMapping("/update/{journeyId}")
    public ResponseEntity<?> updateJourney(@RequestBody JourneyUpdateDTO request,
    		@PathVariable Long journeyId){
    	
    	return journeyService.updateJourney(request, journeyId);
    }
    
    @GetMapping("/all")
    public List<JourneyResponseDTO> getAllJourneys() {
        return service.getAllJourneys();
    }
}

