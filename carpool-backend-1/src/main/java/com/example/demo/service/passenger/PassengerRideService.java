package com.example.demo.service.passenger;

import com.example.demo.dto.passengerRideDTO.PassengerRequestDTO;
import com.example.demo.dto.passengerRideDTO.PassengerResponseDTO;
import com.example.demo.entity.driverEntity.Journey;
import com.example.demo.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PassengerRideService {

    @Autowired
    JourneyRepository journeyRepository;


    public List<PassengerResponseDTO> bookRide(PassengerRequestDTO request) {

        List<Journey> journeys = journeyRepository.findMatchingJourneys(
                request.getStartLocation(),
                request.getDestination()
        );

        return journeys.stream().map(j -> {
            PassengerResponseDTO dto = new PassengerResponseDTO();

//            dto.setDriverName(j.getDriver().getName());
            dto.setStartLocation(request.getStartLocation());
            dto.setDestination(request.getDestination());
            dto.setJourneyDate(j.getJourneyDate());
            dto.setAvailableSeats(j.getAvailableSeats());
            dto.setPrice(j.getPrice());

            return dto;
        }).toList();
    }

}
