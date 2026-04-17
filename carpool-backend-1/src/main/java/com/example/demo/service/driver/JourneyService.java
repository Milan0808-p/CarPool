package com.example.demo.service.driver;

import com.example.demo.dto.driverDtos.JourneyRequestDTO;
import com.example.demo.dto.driverDtos.JourneyUpdateDTO;
import com.example.demo.entity.driverEntity.Driver;
import com.example.demo.entity.driverEntity.Journey;
import com.example.demo.entity.driverEntity.RouteStop;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final DriverRepository driverRepository;

    // 🚀 CREATE JOURNEY
    public JourneyRequestDTO createJourney(JourneyRequestDTO request) {

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Journey journey = new Journey();
        journey.setStartLocation(request.getStartLocation());
        journey.setEndLocation(request.getEndLocation());
        journey.setAvailableSeats(request.getAvailableSeats());
        journey.setPrice(request.getPrice());
        journey.setDate(request.getDate());
        journey.setDepartureTime(request.getDepartureTime());
        journey.setDriver(driver);

        // 🔥 Route Stops
        List<RouteStop> stopList = new ArrayList<>();

        for (int i = 0; i < request.getStops().size(); i++) {
            RouteStop stop = new RouteStop();
            stop.setCityName(request.getStops().get(i));
            stop.setStopOrder(i);
            stop.setJourney(journey);

            stopList.add(stop);
        }

        journey.setStops(stopList);

         journeyRepository.save(journey);

         return request;
    }

    public ResponseEntity<?> updateJourney(JourneyUpdateDTO request,Long journyId) {

        Journey journey = journeyRepository.findById(journyId)
                .orElseThrow(() -> new RuntimeException("Journey not found"));

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        journey.setStartLocation(request.getStartLocation());
        journey.setEndLocation(request.getEndLocation());
        journey.setAvailableSeats(request.getAvailableSeats());
        journey.setPrice(request.getPrice());
        journey.setDate(request.getDate());
        journey.setDepartureTime(request.getDepartureTime());
        journey.setDriver(driver);
        
        journey.getStops().clear();

        List<RouteStop> stopList = new ArrayList<>();

        for (int i = 0; i < request.getStops().size(); i++) {
            RouteStop stop = new RouteStop();
            stop.setCityName(request.getStops().get(i));
            stop.setStopOrder(i);
            stop.setJourney(journey);

            stopList.add(stop);
        }

        journey.getStops().addAll(stopList);

        journeyRepository.save(journey);

        return ResponseEntity.ok("Journey Updated Successfully");
    }

}
