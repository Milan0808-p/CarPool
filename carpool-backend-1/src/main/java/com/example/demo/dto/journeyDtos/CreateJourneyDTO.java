package com.example.demo.dto.journeyDtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CreateJourneyDTO {

    private Long driverId;

    private String startLocation;
    private String endLocation;

    private int availableSeats;
    private double price;

    private LocalDate date;
    private LocalTime departureTime;

    private List<String> stops;


}
