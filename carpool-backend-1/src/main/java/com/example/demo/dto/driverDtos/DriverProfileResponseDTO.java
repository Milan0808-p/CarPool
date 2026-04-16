package com.example.demo.dto.driverDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverProfileResponseDTO {

    private Long userId;
    private String carName;
    private String carNumber;
    private String licenseNumber;

    private String driverImageUrl;
    private String licenseImageUrl;
    private String adharImageUrl;

    private Boolean isVerified;
}