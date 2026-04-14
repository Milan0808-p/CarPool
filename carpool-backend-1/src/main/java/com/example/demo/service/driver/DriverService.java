package com.example.demo.service.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.driverDtos.DriverProfileDTO;
import com.example.demo.entity.authEntity.User;
import com.example.demo.entity.driverEntity.Driver;
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

    public ResponseEntity<?> createProfile(DriverProfileDTO dto, Long userId) {

        try {

            //  1. Get logged-in user (replace with SecurityContext later)
            User user = authRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            //  2. Check existing driver profile
            Driver existing = driverRepository.findByUserId(user.getId());
            
            if(user.getRole().equals("USER")) {
            	return ResponseEntity
                        .badRequest()
                        .body("Only Driver can create profile");
            }
            
            if (existing != null) {
                return ResponseEntity
                        .badRequest()
                        .body("Driver profile already exists");
            }

            // 3. Create folder
            String folderName = "drivers/profile_" + user.getId();

            //  4. Upload images safely
            String driverImageUrl = null;
            String licenseImageUrl = null;
            String adharImageUrl = null;

            if (dto.getDriverImage() != null && !dto.getDriverImage().isEmpty()) {
                driverImageUrl = cloudinaryService.uploadFile(
                        dto.getDriverImage(),
                        folderName,
                        "driver_image"
                );
            }

            if (dto.getLicenseImage() != null && !dto.getLicenseImage().isEmpty()) {
                licenseImageUrl = cloudinaryService.uploadFile(
                        dto.getLicenseImage(),
                        folderName,
                        "license_image"
                );
            }
            
            if (dto.getAdharImage() != null && !dto.getAdharImage().isEmpty()) {
                adharImageUrl = cloudinaryService.uploadFile(
                        dto.getAdharImage(),
                        folderName,
                        "adhar_image"
                );
            }

            //  Optional safety check
            if (driverImageUrl == null || licenseImageUrl == null || adharImageUrl==null) {
                return ResponseEntity
                        .badRequest()
                        .body("all images are required");
            }

            //  5. Create Driver entity
            Driver driver = new Driver();
            driver.setUser(user);
            driver.setCarName(dto.getCarName());
            driver.setCarNumber(dto.getCarNumber());
            driver.setLicenseNumber(dto.getLicenseNumber());
            driver.setProfileImage(driverImageUrl);
            driver.setLicenseImage(licenseImageUrl);
            driver.setAdharImage(adharImageUrl);
            driver.setIsVerified(false);

            //  6. Save
            driverRepository.save(driver);

            return ResponseEntity.ok("Driver profile created successfully ✅");

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

}
