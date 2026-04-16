package com.example.demo.service.driver;

import com.example.demo.dto.driverDtos.DriverProfileResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.DriverProfileDTO;
import com.example.demo.entity.authEntity.Role;
import com.example.demo.entity.authEntity.User;
import com.example.demo.entity.driverEntity.Driver;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.DriverRepository;
import com.example.demo.service.CloudinaryService;

@Service
public class DriverService {

	@Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private AuthRepository authRepository;

    public ResponseEntity<ApiResponse<DriverProfileResponseDTO>> createProfile(DriverProfileDTO dto, Long userId) {

            long start = System.currentTimeMillis();
            //  Get logged-in user (replace with SecurityContext later)
            User user = authRepository.findById(userId)
            		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

            //  Check existing driver profile
            Driver existing = driverRepository.findByUserId(user.getId());
            
            if (user.getRole()==Role.USER){
    		    throw new AccessDeniedException("Only Driver can create profile");
    		}
            
            if (existing != null) {
    		    throw new ResourceAlreadyExistsException("Driver profile already exists");
    		}

            // Create folder
            String folderName = "drivers/profile_" + user.getId();

            // Upload images safely
            String driverImageUrl = null;
            String licenseImageUrl = null;
            String adharImageUrl = null;


            if (dto.getDriverImage() == null || dto.getDriverImage().isEmpty() ||
                    dto.getLicenseImage() == null || dto.getLicenseImage().isEmpty() ||
                    dto.getAdharImage() == null || dto.getAdharImage().isEmpty()) {

            	throw new BadRequestException("All documents (Driver, License, Aadhar) are required");
            }
            

            if (!dto.getDriverImage().isEmpty()) {
                driverImageUrl = cloudinaryService.uploadFile(
                        dto.getDriverImage(),
                        folderName,
                        "driver_image"
                );
            }

            if (!dto.getLicenseImage().isEmpty()) {
                licenseImageUrl = cloudinaryService.uploadFile(
                        dto.getLicenseImage(),
                        folderName,
                        "license_image"
                );
            }
            
            if (!dto.getAdharImage().isEmpty()) {
                adharImageUrl = cloudinaryService.uploadFile(
                        dto.getAdharImage(),
                        folderName,
                        "adhar_image"
                );
            }



            // Create Driver entity
            Driver driver = Driver.builder()
                    .user(user)
                    .carName(dto.getCarName())
                    .carNumber(dto.getCarNumber())
                    .licenseNumber(dto.getLicenseNumber())
                    .profileImage(driverImageUrl)
                    .licenseImage(licenseImageUrl)
                    .adharImage(adharImageUrl)
                    .isVerified(false)
                    .build();

            // Save
            driverRepository.save(driver);

            long end = System.currentTimeMillis();
            System.out.println("Start time "+ start+" end time "+ end);
            System.out.println("Time taken: " + (end - start));

            DriverProfileResponseDTO response = DriverProfileResponseDTO.builder()
                    .userId(user.getId())
                    .carName(driver.getCarName())
                    .carNumber(driver.getCarNumber())
                    .licenseNumber(driver.getLicenseNumber())
                    .driverImageUrl(driverImageUrl)
                    .licenseImageUrl(licenseImageUrl)
                    .adharImageUrl(adharImageUrl)
                    .isVerified(driver.getIsVerified())
                    .build();

            return ResponseEntity.ok(
    	            new ApiResponse<>("success", "Driver profile created successfully ✅", response)
    	    );
            
    }

}
