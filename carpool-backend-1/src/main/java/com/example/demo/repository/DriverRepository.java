package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.driverEntity.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

	Driver findByUserId(Long id);
	
}
