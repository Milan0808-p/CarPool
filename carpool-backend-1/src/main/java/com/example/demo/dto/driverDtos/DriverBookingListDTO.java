package com.example.demo.dto.driverDtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DriverBookingListDTO {

    private Long bookingId;
    private String bookingTime;

    private String passengerName;
    private String passengerPhone;

    private String pickupPoint;
    private String dropPoint;

    private int seatsBooked;
    private double totalPrice;

    private String travelDate;
    private String status;
    private String paymentStatus;
}
