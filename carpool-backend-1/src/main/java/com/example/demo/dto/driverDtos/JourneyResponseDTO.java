package com.example.demo.dto.driverDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
//@AllArgsConstructor
//@NoArgsConstructor
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
    private String numberPlate;
}
