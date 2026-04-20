package com.example.demo.dto.passengerRideDTO;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponseDTO {

    private String driverName;

    private String startLocation;
    private String destination;

    private LocalDate journeyDate;

    private int availableSeats;

    private double price;


}