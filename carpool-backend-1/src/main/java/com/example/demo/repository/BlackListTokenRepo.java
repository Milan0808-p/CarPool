package com.example.demo.repository;

import com.example.demo.entity.authEntity.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BlackListTokenRepo extends JpaRepository<BlackListToken, Long> {


    boolean existsByToken(String token);

    void deleteByExpiryDateBefore(Date now);
}
