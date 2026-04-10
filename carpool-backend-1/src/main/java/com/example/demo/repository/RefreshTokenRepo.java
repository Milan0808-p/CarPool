package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.demo.entity.authEntity.RefreshToken;

import jakarta.transaction.Transactional;



public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long>{
Optional<RefreshToken> findByToken(String token);
    
    @Modifying
    @Transactional
    void deleteByUserId(Long Id);
}
