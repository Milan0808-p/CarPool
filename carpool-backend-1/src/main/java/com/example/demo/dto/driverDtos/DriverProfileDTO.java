package com.example.demo.dto.driverDtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public class DriverProfileDTO {

    @NotBlank(message = "Car model is required")
    private String carModel;

    @NotBlank(message = "Car number is required")
    private String carNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;
    
    private MultipartFile driverImage;
    private MultipartFile licenseImage;
 
}
