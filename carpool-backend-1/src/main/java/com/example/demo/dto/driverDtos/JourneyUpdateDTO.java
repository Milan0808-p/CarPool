package com.example.demo.dto.driverDtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JourneyUpdateDTO {
	
	private Long driverId;

    private String startLocation;
    private String endLocation;

    private int availableSeats;
    private double price;

    private LocalDate date;
    private LocalTime departureTime;

    private List<String> stops;
}
