package com.example.demo.dto.passengerRideDTO;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequestDTO {

    private String startLocation;
    private String destination;
    private Date journeyDate;
}
