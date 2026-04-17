package com.example.demo.entity.journeyEntity;

import com.example.demo.entity.driverEntity.Driver;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "js")
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startLocation;
    private String endLocation;
    private int availableSeats;
    private double price;
    private LocalDate date;
    private LocalTime departureTime;

    // ✅ Stops stored as simple string
    @Column(columnDefinition = "TEXT")
    private String stops;

    // 🔥 FIX: pgvector mapping
    @Column(name = "route_embedding", columnDefinition = "vector(6)")
    @org.hibernate.annotations.ColumnTransformer(write = "?::vector")
    private String routeEmbedding;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
