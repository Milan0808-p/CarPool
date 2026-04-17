package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.driverEntity.Journey;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    @Query("""
        SELECT j FROM Journey j
        JOIN FETCH j.stops
        JOIN FETCH j.driver d
        JOIN FETCH d.user
    """)
    List<Journey> findAllWithDetails();

    List<Journey> findMatchingJourneys(String startLocation, String destination);
}
