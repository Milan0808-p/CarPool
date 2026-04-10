package com.example.demo.entity.authEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String carModel;
    private String carNumber;
    private String licenseNumber;

    private String profileImage;
    private String licenseImage;

    private Boolean isVerified = false;
}
