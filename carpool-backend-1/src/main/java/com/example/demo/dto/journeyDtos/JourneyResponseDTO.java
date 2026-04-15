package com.example.demo.dto.journeyDtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class JourneyResponseDTO {

    private Long journeyId;
    private String startLocation;
    private String endLocation;

    private LocalDate date;
    private LocalTime departureTime;

    private double price;
    private int availableSeats;

    private List<String> stops;

    private String driverName;
    private String carName;
}
