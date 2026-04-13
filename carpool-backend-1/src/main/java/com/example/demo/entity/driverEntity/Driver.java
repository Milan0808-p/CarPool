package com.example.demo.entity.driverEntity;

import com.example.demo.dto.authDtos.AuthResponse;
import com.example.demo.entity.authEntity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull(message = "User is required")
    private User user;

    @NotBlank(message = "Car model is required")
    private String carModel;

    @NotBlank(message = "Car number is required")
    private String carNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;


    private String profileImage;
    private String licenseImage;
    private String adharImage;

    private Boolean isVerified = false;
}
