package com.example.demo.controller;
import com.example.demo.dto.journeyDtos.CreateJourneyDTO;
import com.example.demo.dto.journeyDtos.JourneyResponseDTO;
import com.example.demo.entity.journeyEntity.Journey;
import com.example.demo.service.Journey.JourneyService;
import com.example.demo.service.Journey.PassengerJourneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/journeys")
@RequiredArgsConstructor
public class JourneyController {

    private final JourneyService journeyService;

    private final PassengerJourneyService service;

//    // 🚀 POST JOURNEY
//    @PostMapping("/create")
//    public CreateJourneyDTO createJourney(@RequestBody CreateJourneyDTO request) {
//        return journeyService.createJourney(request);
//    }

    @GetMapping("/all")
    public List<JourneyResponseDTO> getAllJourneys() {
        return service.getAllJourneys();
    }

    // 🚀 Create Journey
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateJourneyDTO request){
        try {
            CreateJourneyDTO res  =journeyService.createJourney(request);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    // 🔥 Search by vector
    @PostMapping("/search")
    public List<Journey> search(@RequestBody List<Double> vector) {

        return journeyService.searchSimilarRoutes(vector);
    }
}

