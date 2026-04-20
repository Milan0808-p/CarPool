//package com.example.demo.service.driver;
//
//import com.example.demo.dto.ApiResponse;
//import com.example.demo.dto.driverDtos.JourneyRequestDTO;
//import com.example.demo.dto.driverDtos.JourneyResponseDTO;
//import com.example.demo.dto.driverDtos.JourneyUpdateDTO;
//import com.example.demo.entity.driverEntity.Driver;
//import com.example.demo.entity.driverEntity.Journey;
//import com.example.demo.entity.driverEntity.RouteStop;
//import com.example.demo.exception.ResourceAlreadyExistsException;
//import com.example.demo.exception.ResourceNotFoundException;
//import com.example.demo.repository.DriverRepository;
//import com.example.demo.repository.JourneyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class JourneyService {
//
//    private final JourneyRepository journeyRepository;
//    private final DriverRepository driverRepository;
//
//    // CREATE JOURNEY
//    public ResponseEntity<ApiResponse<JourneyResponseDTO>> createJourney(JourneyRequestDTO request) {
//
//        Driver driver = driverRepository.findById(request.getDriverId())
//                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
//        
//        boolean alreadyExists = journeyRepository
//                .existsByDriverIdAndDateAndStartLocationAndEndLocation(
//                        request.getDriverId(),
//                        request.getDate(),
//                        request.getStartLocation(),
//                        request.getEndLocation()
//                );
//
//        if (alreadyExists) {
//            throw new ResourceAlreadyExistsException("Same journey already exists");
//        }
//        
//        Journey journey = Journey.builder()
//                .startLocation(request.getStartLocation())
//                .endLocation(request.getEndLocation())
//                .availableSeats(request.getAvailableSeats())
//                .price(request.getPrice())
//                .date(request.getDate())
//                .departureTime(request.getDepartureTime())
//                .driver(driver)
//                .build();
//
//        // Route Stops
//        List<RouteStop> stopList = new ArrayList<>();
//
//        for (int i = 0; i < request.getStops().size(); i++) {
//        	RouteStop stop = RouteStop.builder()
//        	        .cityName(request.getStops().get(i))
//        	        .stopOrder(i)
//        	        .journey(journey)
//        	        .build();
//
//            stopList.add(stop);
//        }
//
//        journey.setStops(stopList);
//
//        journeyRepository.save(journey);
//        
//        List<String> stopNames = journey.getStops()
//                .stream()
//                .map(RouteStop::getCityName)
//                .toList();
//        
//        JourneyResponseDTO response= JourneyResponseDTO.builder()
//        		.journeyId(journey.getId())
//        		.startLocation(journey.getStartLocation())
//        		.endLocation(journey.getEndLocation())
//        		.departureTime(journey.getDepartureTime())
//        		.price(journey.getPrice())
//        		.availableSeats(journey.getAvailableSeats())
//        		.driverName(journey.getDriver().getUser().getUsername())
//        		.carName(journey.getDriver().getCarName())
//        		.stops(stopNames)
//        		.date(journey.getDate())
//        		.build();
//
//        return ResponseEntity.ok(
//	            new ApiResponse<>("success", "Ride Creatation successful", response)
//	    );
//    }
//
//    public ResponseEntity<ApiResponse<JourneyResponseDTO>> updateJourney(JourneyUpdateDTO request,Long journyId) {
//
//        Journey journey = journeyRepository.findById(journyId)
//                .orElseThrow(() -> new ResourceNotFoundException("Journey not found"));
//
//        Driver driver = driverRepository.findById(request.getDriverId())
//                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
//        
//        journey.setStartLocation(request.getStartLocation());
//        journey.setEndLocation(request.getEndLocation());
//        journey.setAvailableSeats(request.getAvailableSeats());
//        journey.setPrice(request.getPrice());
//        journey.setDate(request.getDate());
//        journey.setDepartureTime(request.getDepartureTime());
//        journey.setDriver(driver);
//
//
//        if (request.getStops() != null) {
//            List<RouteStop> existingStops = journey.getStops();
//            List<String> newStops = request.getStops();
//
//            for (int i = 0; i < newStops.size(); i++) {
//                if (i < existingStops.size()) {
//                    RouteStop stop = existingStops.get(i);
//                    stop.setCityName(newStops.get(i));
//                    stop.setStopOrder(i);
//                } else {
//                    RouteStop newStop = RouteStop.builder()
//                            .cityName(newStops.get(i))
//                            .stopOrder(i)
//                            .journey(journey)
//                            .build();
//                    existingStops.add(newStop);
//                }
//            }
//
//            if (existingStops.size() > newStops.size()) {
//                existingStops.subList(newStops.size(), existingStops.size()).clear();
//            }
//        }
//
//        journeyRepository.save(journey);
//        
//        List<String> stopNames = journey.getStops()
//                .stream()
//                .map(RouteStop::getCityName)
//                .toList();
//        
//        JourneyResponseDTO response= JourneyResponseDTO.builder()
//        		.journeyId(journey.getId())
//        		.startLocation(journey.getStartLocation())
//        		.endLocation(journey.getEndLocation())
//        		.departureTime(journey.getDepartureTime())
//        		.price(journey.getPrice())
//        		.availableSeats(journey.getAvailableSeats())
//        		.driverName(journey.getDriver().getUser().getUsername())
//        		.carName(journey.getDriver().getCarName())
//        		.stops(stopNames)
//        		.date(journey.getDate())
//        		.build();
//
//        return ResponseEntity.ok(
//	            new ApiResponse<>("success", "Ride Updatation successful", response)
//	    );
//    }
//
//}
