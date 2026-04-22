package com.example.demo.dto.passengerRideDTO;

import lombok.Data;

@Data
public class PassengerBookingRequestDTO {
    private String journeyId;
    private Long userId;
    private int seats;
    private String pickupPoint;
    private String dropPoint;
}

