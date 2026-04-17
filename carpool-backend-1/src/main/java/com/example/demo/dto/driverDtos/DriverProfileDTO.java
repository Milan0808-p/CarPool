package com.example.demo.dto.driverDtos;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverProfileDTO {

    @NotBlank(message = "Car name is required")
    private String carName;

    @NotBlank(message = "Car number is required")
    private String carNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;
    private MultipartFile driverImage;
    private MultipartFile licenseImage;
    private MultipartFile adharImage;
    
}
