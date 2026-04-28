package com.example.demo.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.authEntity.User;
import com.example.demo.entity.driverEntity.Journey;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    @Query("""
        SELECT j FROM Journey j
        JOIN FETCH j.stops
        JOIN FETCH j.driver d
        JOIN FETCH d.user
    """)
    List<Journey> findAllWithDetails();

	boolean existsByDriver_User_IdAndDateAndStartLocationAndEndLocation(Long driverId, LocalDate date, String startLocation,
			String endLocation);
    List<Journey> findByStartLocationAndEndLocation(String startLocation, String endLocation);



//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT j FROM Journey j WHERE j.id = :id")
//    Optional<Journey> findByIdForUpdate(Long id);

    Optional<Journey> findByPublicId(String publicId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT j FROM Journey j WHERE j.publicId = :publicId")
    Optional<Journey> findByPublicIdForUpdate(@Param("publicId") String publicId);

}
