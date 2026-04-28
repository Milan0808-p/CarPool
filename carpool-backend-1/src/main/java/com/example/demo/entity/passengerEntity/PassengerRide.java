package com.example.demo.entity.passengerEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String startLocation;
    private String destination;
    @Temporal(TemporalType.DATE)
    private Date journeyDate;

}
