package com.example.demo.repository;

import com.example.demo.entity.journeyEntity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    @Query("""
        SELECT j FROM Journey j
        JOIN FETCH j.stops
        JOIN FETCH j.driver d
        JOIN FETCH d.user
    """)
    List<Journey> findAllWithDetails();
}
