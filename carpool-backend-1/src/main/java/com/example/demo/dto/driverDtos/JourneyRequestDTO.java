package com.example.demo.dto.driverDtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.*;

@Data
public class JourneyRequestDTO {

//	@NotBlank(message = "Driver ID is required")
//    private Long driverId;

    @NotBlank(message = "Start location is required")
    private String startLocation;

    @NotBlank(message = "End location is required")
    private String endLocation;

    @Min(value = 1, message = "Available seats must be at least 1")
    private int availableSeats;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Journey date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotEmpty(message = "Stops cannot be empty")
    private List<@NotBlank(message = "Stop name cannot be blank") String> stops;
}
