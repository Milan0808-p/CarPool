package com.example.demo.dto.authDtos;

import jakarta.validation.constraints.NotBlank;

public class DriverRegisterDTO {

    @NotBlank(message = "Car model is required")
    private String carModel;

    @NotBlank(message = "Car number is required")
    private String carNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    // getters and setters
}
