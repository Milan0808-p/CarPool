package com.example.demo.service.driver;

import com.example.demo.dto.driverDtos.DriverProfileResponseDTO;
import com.example.demo.dto.driverDtos.JourneyRequestDTO;
import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.driverDtos.JourneyUpdateDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.DriverBookingListDTO;
import com.example.demo.dto.driverDtos.DriverProfileDTO;
import com.example.demo.entity.authEntity.Role;
import com.example.demo.entity.authEntity.User;
import com.example.demo.entity.driverEntity.Driver;
import com.example.demo.entity.driverEntity.Journey;
import com.example.demo.entity.driverEntity.RouteStop;
import com.example.demo.entity.passengerEntity.BookingStatus;
import com.example.demo.entity.passengerEntity.PassengerBooking;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.DriverRepository;
import com.example.demo.repository.JourneyRepository;
import com.example.demo.repository.PassengerBookingRepository;
import com.example.demo.service.CloudinaryService;

@Service
public class DriverService {

	@Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private AuthRepository authRepository;
    
    @Autowired
    private JourneyRepository journeyRepository;
    
    @Autowired
    private PassengerBookingRepository passengerBookingRepository;
    
    public ResponseEntity<ApiResponse<DriverProfileResponseDTO>> createProfile(DriverProfileDTO dto, Long userId) {

            long start = System.currentTimeMillis();
            //  Get logged-in user (replace with SecurityContext later)
            User user = authRepository.findById(userId)
            		.orElseThrow(() -> new ResourceNotFoundException("User not found"));

            //  Check existing driver profile
            Driver existing = driverRepository.findByUserId(user.getId());
            
//                  .orElseThrow(() -> new ResourceAlreadyExistsException("Driver profile already exists"));
            
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
                    .userId(user.getPublicId())
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
    
 // CREATE JOURNEY
    public ResponseEntity<ApiResponse<JourneyResponseDTO>> createJourney(JourneyRequestDTO request, Long driverId) {
    	
    	if (request.getDate().isBefore( LocalDate.now())) {
    	    throw new InvalidCredentialsException("Past date not allowed");
    	}
    	
    	Driver driver = driverRepository.findByUserId(driverId);
//    	        .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    	if (driver == null) {
		    throw new ResourceNotFoundException("Driver not found");
		}
//    	 User driver = authRepository.findById(driverId)
//               .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
    	 
        boolean alreadyExists = journeyRepository
                .existsByDriver_User_IdAndDateAndStartLocationAndEndLocation(
                		driverId,
                        request.getDate(),
                        request.getStartLocation(),
                        request.getEndLocation()
                );

        if (alreadyExists) {
            throw new ResourceAlreadyExistsException("Same journey already exists");
        }
        
        Journey journey = Journey.builder()
                .startLocation(request.getStartLocation())
                .endLocation(request.getEndLocation())
                .availableSeats(request.getAvailableSeats())
                .price(request.getPrice())
                .date(request.getDate())
                .departureTime(request.getDepartureTime())
                .driver(driver)
                .build();

        // Route Stops
        List<RouteStop> stopList = new ArrayList<>();

        for (int i = 0; i < request.getStops().size(); i++) {
        	RouteStop stop = RouteStop.builder()
        	        .cityName(request.getStops().get(i))
        	        .stopOrder(i)
        	        .journey(journey)
        	        .build();

            stopList.add(stop);
        }

        journey.setStops(stopList);

        journeyRepository.save(journey);
        
        List<String> stopNames = journey.getStops()
                .stream()
                .map(RouteStop::getCityName)
                .toList();
        
        JourneyResponseDTO response= JourneyResponseDTO.builder()
        		.journeyId(journey.getPublicId())
        		.startLocation(journey.getStartLocation())
        		.endLocation(journey.getEndLocation())
        		.departureTime(journey.getDepartureTime())
        		.price(journey.getPrice())
        		.availableSeats(journey.getAvailableSeats())
        		.driverName(journey.getDriver().getUser().getUsername())
        		.carName(journey.getDriver().getCarName())
                .numberPlate(journey.getDriver().getCarNumber())
        		.stops(stopNames)
        		.date(journey.getDate())
        		.build();

        return ResponseEntity.ok(
	            new ApiResponse<>("success", "Ride Creation successful", response)
	    );
    }

    public ResponseEntity<ApiResponse<JourneyResponseDTO>> updateJourney(JourneyUpdateDTO request,String journyId, Long userId) {
    	
    	if (request.getDate().isBefore( LocalDate.now())) {
    	    throw new InvalidCredentialsException("Past date not allowed");
    	}
    	
        Journey journey = journeyRepository.findByPublicId(journyId)
                .orElseThrow(() -> new ResourceNotFoundException("Journey not found"));

        Driver driver = journey.getDriver();

        if (driver == null ||
            driver.getUser() == null ||
            !Objects.equals(driver.getUser().getId(), userId)) {
            throw new AccessDeniedException("You are not authorized to update this journey");
        }
        
        journey.setStartLocation(request.getStartLocation());
        journey.setEndLocation(request.getEndLocation());
        journey.setAvailableSeats(request.getAvailableSeats());
        journey.setPrice(request.getPrice());
        journey.setDate(request.getDate());
        journey.setDepartureTime(request.getDepartureTime());

        if (request.getStops() != null) {
            List<RouteStop> existingStops = journey.getStops();
            List<String> newStops = request.getStops();

            for (int i = 0; i < newStops.size(); i++) {
                if (i < existingStops.size()) {
                    RouteStop stop = existingStops.get(i);
                    stop.setCityName(newStops.get(i));
                    stop.setStopOrder(i);
                } else {
                    RouteStop newStop = RouteStop.builder()
                            .cityName(newStops.get(i))
                            .stopOrder(i)
                            .journey(journey)
                            .build();
                    existingStops.add(newStop);
                }
            }

            if (existingStops.size() > newStops.size()) {
                existingStops.subList(newStops.size(), existingStops.size()).clear();
            }
        }

        journeyRepository.save(journey);
        
        List<String> stopNames = journey.getStops()
                .stream()
                .map(RouteStop::getCityName)
                .toList();
        
        JourneyResponseDTO response= JourneyResponseDTO.builder()
        		.journeyId(journey.getPublicId())
        		.startLocation(journey.getStartLocation())
        		.endLocation(journey.getEndLocation())
        		.departureTime(journey.getDepartureTime())
        		.price(journey.getPrice())
        		.availableSeats(journey.getAvailableSeats())
        		.driverName(journey.getDriver().getUser().getUsername())
        		.carName(journey.getDriver().getCarName())
        		.stops(stopNames)
        		.date(journey.getDate())
        		.build();

        return ResponseEntity.ok(
	            new ApiResponse<>("success", "Ride Updatation successful", response)
	    );
    }

    public ResponseEntity<ApiResponse<Void>> deleteJourney(String journeyId, Long userId) {

        Journey journey = journeyRepository.findByPublicId(journeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Journey not found"));

        Driver driver = journey.getDriver();

        if (driver == null ||
            driver.getUser() == null ||
            !Objects.equals(driver.getUser().getId(), userId)) {

            throw new AccessDeniedException("You are not authorized to delete this journey");
        }

        journeyRepository.delete(journey);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Journey deleted successfully", null)
        );
    }

    public ResponseEntity<ApiResponse<List<DriverBookingListDTO>>> getBookings(Long userId) {

        List<PassengerBooking> bookings = passengerBookingRepository.findByJourney_Driver_User_Id(userId);

        List<DriverBookingListDTO> response = bookings.stream()
                .map(this::convertToDTO)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Booking list fetched successfully", response)
        );
    }
    
    private DriverBookingListDTO convertToDTO(PassengerBooking b) {

        return DriverBookingListDTO.builder()
                .bookingId(b.getPublicId())
                .bookingTime(b.getBookingTime().toString())
                .passengerName(b.getPassenger().getUsername())
                .passengerPhone(b.getPassenger().getPhone())
                .pickupPoint(b.getPickupPoint())
                .dropPoint(b.getDropPoint())
                .seatsBooked(b.getSeatsBooked())
                .totalPrice(b.getTotalPrice())
                .travelDate(b.getTravelDate().toString())
                .status(b.getStatus().name())
                .paymentStatus(b.getPaymentStatus().name())
                .build();
    }

	public ResponseEntity<ApiResponse<DriverBookingListDTO>> updateBookingStatus(String bookingId, Long userId,
			String status) {

		PassengerBooking booking = passengerBookingRepository.findByPublicId(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

		Driver driver = booking.getJourney().getDriver();
		
        if (driver == null ||
            driver.getUser() == null ||
            !Objects.equals(driver.getUser().getId(), userId)) {

            throw new AccessDeniedException("You are not authorized to update this journey");
        }

		booking.setStatus(BookingStatus.valueOf(status.toUpperCase()));

		passengerBookingRepository.save(booking);

		DriverBookingListDTO dto = convertToDTO(booking);

		return ResponseEntity.ok(new ApiResponse<>("success", "Booking status updated", dto));
	}

}
