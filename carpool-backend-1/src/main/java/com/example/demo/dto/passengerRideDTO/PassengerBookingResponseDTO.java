package com.example.demo.dto.passengerRideDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@Transactional
public class PassengerBookingResponseDTO {

    private String bookingId;

    // Passenger
    private String passengerName;

    // Journey info
    private Long journeyId;
    private String startLocation;
    private String endLocation;
    private LocalDate travelDate;
    private LocalTime departureTime;

    // Booking details
    private Integer seatsBooked;
    private Double totalPrice;

    private String pickupPoint;
    private String dropPoint;

    // Status
    private String bookingStatus;
    private String paymentStatus;

    // Driver & Vehicle
    private String driverName;
    private String carName;
    private String numberPlate;

    // Time
    private LocalDateTime bookingTime;
}
