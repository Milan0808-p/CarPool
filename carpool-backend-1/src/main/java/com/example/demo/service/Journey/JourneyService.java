package com.example.demo.service.Journey;

import com.example.demo.dto.journeyDtos.CreateJourneyDTO;
import com.example.demo.entity.driverEntity.Driver;
import com.example.demo.entity.journeyEntity.Journey;
import com.example.demo.entity.journeyEntity.RouteStop;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyService {

    private final JourneyRepository journeyRepository;
    private final DriverRepository driverRepository;

    // 🚀 CREATE JOURNEY
    public CreateJourneyDTO createJourney(CreateJourneyDTO request) {

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

}
