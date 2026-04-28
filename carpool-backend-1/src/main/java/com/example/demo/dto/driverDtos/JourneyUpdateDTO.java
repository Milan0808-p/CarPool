package com.example.demo.dto.driverDtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JourneyUpdateDTO {
	
//	@NotNull(message = "Driver ID is required")
//    private Long driverId;

    @NotBlank(message = "Start location is required")
    private String startLocation;

    @NotBlank(message = "End location is required")
    private String endLocation;

    @Min(value = 1, message = "Available seats must be at least 1")
    private int availableSeats;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @FutureOrPresent(message = "Date cannot be in the past")
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    private List<String> stops;
}
