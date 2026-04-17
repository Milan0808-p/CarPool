package com.example.demo.repository;

import com.example.demo.entity.journeyEntity.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    // ✅ Correct version
    @Query("""
        SELECT j FROM Journey j
        JOIN FETCH j.driver d
        JOIN FETCH d.user
    """)
    List<Journey> findAllWithDetails();

    // 🔥 Vector similarity search
    @Query(value = """
        SELECT * FROM journey
        ORDER BY route_embedding <-> CAST(:embedding AS vector)
        LIMIT 5
    """, nativeQuery = true)
    List<Journey> findNearest(String embedding);
}
