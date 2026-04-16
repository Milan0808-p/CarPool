package com.example.demo.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.demo.entity.authEntity.User;
import com.example.demo.repository.AuthRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AuthRepository repo;

    public CustomUserDetailService(AuthRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String userIdStr) throws UsernameNotFoundException {

        Long userId = Long.parseLong(userIdStr);

        User user = repo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(user.getId()))
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}