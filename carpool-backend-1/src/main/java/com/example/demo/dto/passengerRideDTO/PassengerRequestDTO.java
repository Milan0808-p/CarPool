package com.example.demo.dto.passengerRideDTO;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PassengerRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String startLocation;
    private String destination;
    @Temporal(TemporalType.DATE)
    private Date journeyDate;


}
