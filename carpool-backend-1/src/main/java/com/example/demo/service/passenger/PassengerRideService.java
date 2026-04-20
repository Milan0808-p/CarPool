package com.example.demo.service.passenger;

import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.passengerRideDTO.PassengerRequestDTO;
import com.example.demo.dto.passengerRideDTO.PassengerResponseDTO;
import com.example.demo.entity.driverEntity.Journey;
import com.example.demo.repository.JourneyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PassengerRideService {

    @Autowired
    JourneyRepository journeyRepository;


    public List<JourneyResponseDTO> bookRide(PassengerRequestDTO request) {

        List<Journey> journeys = journeyRepository
                .findByStartLocationAndEndLocation(
                        request.getStartLocation(),
                        request.getDestination()
                );

        return journeys.stream().map(j ->
                JourneyResponseDTO.builder()
                        .journeyId(j.getId())
                        .startLocation(j.getStartLocation())
                        .endLocation(j.getEndLocation())
                        .date(j.getDate())
                        .departureTime(j.getDepartureTime())
                        .availableSeats(j.getAvailableSeats())
                        .price(j.getPrice())
                        .numberPlate(j.getDriver().getCarNumber())
                        .carName(j.getDriver().getCarName())
                        .driverName(j.getDriver().getUser().getUsername())

                        .stops(
                                j.getStops() != null
                                        ? j.getStops().stream()
                                        .map(s -> s.getCityName())
                                        .toList()
                                        : null
                        )

//                        .numberPlate(
//                                j.getDriver() != null ? j.getDriver().getCarNumber() : null
//                        )
                        .build()
        ).toList();
    }
}
