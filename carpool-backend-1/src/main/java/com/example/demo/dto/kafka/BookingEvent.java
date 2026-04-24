package com.example.demo.dto.kafka;

import com.example.demo.dto.passengerRideDTO.PassengerBookingRequestDTO;

import lombok.Data;

@Data
public class BookingEvent {

	private String bookingId;
	private String userId;
	private String driverId;
	private String status;

	private String driverEmail;
	private String passengerEmail;

	private String pickupPoint;
	private String dropPoint;

}
