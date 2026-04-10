package com.example.demo.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.authEntity.User;

@Repository
public interface AuthRepository extends JpaRepository<User, Long>{

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);  


}
