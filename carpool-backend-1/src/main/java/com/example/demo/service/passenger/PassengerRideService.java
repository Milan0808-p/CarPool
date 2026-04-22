package com.example.demo.service.passenger;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.driverDtos.JourneyResponseDTO;
import com.example.demo.dto.passengerRideDTO.PassengerBookingRequestDTO;
import com.example.demo.dto.passengerRideDTO.PassengerBookingResponseDTO;
import com.example.demo.dto.passengerRideDTO.PassengerRequestDTO;
import com.example.demo.entity.authEntity.User;
import com.example.demo.entity.driverEntity.Journey;
import com.example.demo.entity.passengerEntity.PassengerBooking;
import com.example.demo.exception.DuplicateBookingException;
import com.example.demo.exception.InsufficientSeatsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AuthRepository;
import com.example.demo.repository.JourneyRepository;
import com.example.demo.repository.PassengerBookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerRideService {

    @Autowired
    JourneyRepository journeyRepository;

    @Autowired
    private PassengerBookingRepository bookingRepository;

    @Autowired
    private AuthRepository authRepository;


    public ResponseEntity<ApiResponse<List<JourneyResponseDTO>>> searchRide(PassengerRequestDTO request) {

        List<Journey> journeys = journeyRepository
                .findByStartLocationAndEndLocation(
                        request.getStartLocation(),
                        request.getDestination()
                );

//<<<<<<< HEAD
//        return journeys.stream().map(j ->
//                JourneyResponseDTO.builder()
//                        .journeyId(j.getPublicId())
//                        .startLocation(j.getStartLocation())
//                        .endLocation(j.getEndLocation())
//                        .date(j.getDate())
//                        .departureTime(j.getDepartureTime())
//                        .availableSeats(j.getAvailableSeats())
//                        .price(j.getPrice())
//                        .numberPlate(j.getDriver().getCarNumber())
//                        .carName(j.getDriver().getCarName())
//                        .driverName(j.getDriver().getUser().getUsername())
//=======
        List<JourneyResponseDTO> rides = journeys.stream().map(j ->
                        JourneyResponseDTO.builder()
                                .journeyId(j.getPublicId())
                                .startLocation(j.getStartLocation())
                                .endLocation(j.getEndLocation())
                                .date(j.getDate())
                                .departureTime(j.getDepartureTime())
                                .availableSeats(j.getAvailableSeats())
                                .price(j.getPrice())
                                .numberPlate(j.getDriver().getCarNumber())
                                .carName(j.getDriver().getCarName())
                                .driverName(j.getDriver().getUser().getUsername())
//>>>>>>> eb53a97da11d13916d5f5eb42cd02f33cebccd93

                                .stops(
                                        j.getStops() != null
                                                ? j.getStops().stream()
                                                .map(s -> s.getCityName())
                                                .toList()
                                                : null
                                )

//                        .numberPlate(
//                                j.getDriver() != null ? j.getDriver().getCarNumber() : null
//                        )
                                .build()
        ).toList();

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Matched rides found", rides)
        );

    }

    @Transactional
    public ResponseEntity<ApiResponse<PassengerBookingResponseDTO>> bookRide(PassengerBookingRequestDTO request) {

        // 1. Fetch journey WITH LOCK
        Journey journey = journeyRepository.findByPublicIdForUpdate(request.getJourneyId())
                .orElseThrow(() -> new ResourceNotFoundException("Journey not found"));

        // 2. Fetch user
        User user = authRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 3. Prevent duplicate booking
        boolean alreadyBooked = bookingRepository
                .existsByPassengerIdAndJourneyId(user.getId(), journey.getId());

        if (alreadyBooked) {
            throw new DuplicateBookingException("You already booked this ride");
        }

        // 4. Validate seats (AFTER LOCK )
        if (journey.getAvailableSeats() < request.getSeats()) {
            throw new InsufficientSeatsException("Not enough seats available");
        }

        // 5. Update seats FIRST
        journey.setAvailableSeats(
                journey.getAvailableSeats() - request.getSeats()
        );

        // 6. Create booking
        PassengerBooking booking = new PassengerBooking();
        booking.setPassenger(user);
        booking.setJourney(journey);
        booking.setSeatsBooked(request.getSeats());
        booking.setPickupPoint(request.getPickupPoint());
        booking.setDropPoint(request.getDropPoint());
        booking.setTravelDate(journey.getDate());

        // 7. Price calculation
        double totalPrice = journey.getPrice() * request.getSeats();
        booking.setTotalPrice(totalPrice);

        // 8. Save booking
        bookingRepository.save(booking);

        // (No need to explicitly save journey if managed by JPA)

        // 9. Return response
//<<<<<<< HEAD
//        return PassengerBookingResponseDTO.builder()
//                .bookingId(booking.getPublicId())
//=======
        PassengerBookingResponseDTO rideBooked = PassengerBookingResponseDTO.builder()
                .bookingId(booking.getPublicId())
//>>>>>>> eb53a97da11d13916d5f5eb42cd02f33cebccd93
                .passengerName(user.getUsername())
                .journeyId(journey.getId())
                .startLocation(journey.getStartLocation())
                .endLocation(journey.getEndLocation())
                .travelDate(booking.getTravelDate())
                .departureTime(journey.getDepartureTime())
                .seatsBooked(booking.getSeatsBooked())
                .totalPrice(booking.getTotalPrice())
                .pickupPoint(booking.getPickupPoint())
                .dropPoint(booking.getDropPoint())
                .bookingStatus(booking.getStatus().name())
                .paymentStatus(booking.getPaymentStatus().name())
                .driverName(
                        journey.getDriver() != null && journey.getDriver().getUser() != null
                                ? journey.getDriver().getUser().getUsername()
                                : null
                )
                .carName(journey.getDriver() != null ? journey.getDriver().getCarName() : null)
                .numberPlate(journey.getDriver() != null ? journey.getDriver().getCarNumber() : null)
                .bookingTime(booking.getBookingTime())
                .build();


        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Will notify you,when captain will accept", rideBooked)
        );
    }


}
