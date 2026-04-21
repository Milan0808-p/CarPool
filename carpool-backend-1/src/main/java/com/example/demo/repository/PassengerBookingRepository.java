package com.example.demo.repository;

import com.example.demo.entity.driverEntity.Journey;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
import com.example.demo.entity.passengerEntity.PassengerBooking;

public interface PassengerBookingRepository extends JpaRepository<PassengerBooking, Long> {

    // Get all bookings for journeys created by a driver

	@Query("SELECT pb FROM PassengerBooking pb " +
	           "JOIN FETCH pb.journey j " +
	           "JOIN FETCH pb.passenger p " +
	           "WHERE j.driver.id = :driverId")
	    List<PassengerBooking> findByDriverId(@Param("driverId") Long driverId);

	boolean existsByPassengerIdAndJourneyId(Long id, Long id1);

	List<PassengerBooking> findByJourney_Driver_User_Id(Long userId);
}
