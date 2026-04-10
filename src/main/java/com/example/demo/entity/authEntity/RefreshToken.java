package com.example.demo.entity.authEntity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "refresh_tokens")
public class RefreshToken {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     String token;

     Date expiryDate;

    @ManyToOne
    private User user;
}
