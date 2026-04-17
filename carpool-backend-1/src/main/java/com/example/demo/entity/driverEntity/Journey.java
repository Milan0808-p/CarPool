package com.example.demo.entity.driverEntity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startLocation;
    private String endLocation;
    private int availableSeats;
    private double price;
    private LocalDate journeyDate;
    private LocalTime departureTime;

    @OneToMany(mappedBy = "journey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("stopOrder ASC")
    @JsonManagedReference
    private List<RouteStop> stops;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

}
