package com.example.demo.entity.driverEntity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor 
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    protected String publicId;
    
    private String startLocation;
    private String endLocation;
    private int availableSeats;
    private double price;
    private LocalDate date;
    private LocalTime departureTime;

//    @OneToMany(
//    	    mappedBy = "journey",
//    	    cascade = CascadeType.ALL,
//    	    fetch = FetchType.LAZY,
//    	    orphanRemoval = true //automatic delete child entity data
//    	)
//    @OrderBy("stopOrder ASC")
//    @JsonManagedReference
//    private List<RouteStop> stops;
//
//    @ManyToOne
//    @JoinColumn(name = "driver_id")
//    private Driver driver;
    
    @OneToMany(
    	    mappedBy = "journey",
    	    cascade = CascadeType.ALL,
    	    fetch = FetchType.LAZY,
    	    orphanRemoval = true //automatic delete child entity data
    	)
    @ToString.Exclude
    private List<RouteStop> stops;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @ToString.Exclude
    private Driver driver;
    
    @PrePersist
    protected void onCreate() {
        this.publicId = UUID.randomUUID().toString();
    }
}
