package com.example.demo.service.Journey;

import com.example.demo.dto.journeyDtos.CreateJourneyDTO;
import com.example.demo.dto.journeyDtos.JourneyResponseDTO;
import com.example.demo.entity.journeyEntity.Journey;
import com.example.demo.entity.journeyEntity.RouteStop;
import com.example.demo.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerJourneyService {

    private final JourneyRepository journeyRepository;
    public List<JourneyResponseDTO> getAllJourneys() {

        List<Journey> journeys = journeyRepository.findAllWithDetails();

        List<JourneyResponseDTO> responseList = new ArrayList<>();

        for (Journey j : journeys) {

            JourneyResponseDTO response = JourneyResponseDTO.builder()
                    .journeyId(j.getId())
                    .startLocation(j.getStartLocation())
                    .endLocation(j.getEndLocation())
                    .date(j.getDate())
                    .departureTime(j.getDepartureTime())
                    .price(j.getPrice())
                    .availableSeats(j.getAvailableSeats())
                    .stops(
                            j.getStops().stream()
                                    .map(RouteStop::getCityName)
                                    .toList()
                    )
                    .driverName(j.getDriver().getUser().getUsername())
                    .carName(j.getDriver().getCarName())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

}
