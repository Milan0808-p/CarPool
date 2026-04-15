package com.example.demo.entity.journeyEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cityName;
    private int stopOrder;

    @ManyToOne
    @JoinColumn(name = "journey_id")
    @JsonBackReference
    private Journey journey;
}
