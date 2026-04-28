package com.example.demo.entity.driverEntity;

import com.example.demo.entity.authEntity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    protected String publicId;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull(message = "User is required")
    private User user;

    @NotBlank(message = "Car model is required")
    private String carName;

    @NotBlank(message = "Car number is required")
    private String carNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;


    private String profileImage;
    private String licenseImage;
    private String adharImage;

    private Boolean isVerified = false;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Journey> journeys;
    
    @PrePersist
    protected void onCreate() {
        this.publicId = UUID.randomUUID().toString();
    }
}
