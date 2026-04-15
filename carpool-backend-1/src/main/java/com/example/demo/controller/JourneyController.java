package com.example.demo.controller;
import com.example.demo.dto.journeyDtos.CreateJourneyDTO;
import com.example.demo.dto.journeyDtos.JourneyResponseDTO;
import com.example.demo.service.Journey.JourneyService;
import com.example.demo.service.Journey.PassengerJourneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journeys")
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService journeyService;

    private final PassengerJourneyService service;

    // 🚀 POST JOURNEY
    @PostMapping("/create")
    public CreateJourneyDTO createJourney(@RequestBody CreateJourneyDTO request) {
        return journeyService.createJourney(request);
    }

    @GetMapping("/all")
    public List<JourneyResponseDTO> getAllJourneys() {
        return service.getAllJourneys();
    }
}

