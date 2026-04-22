package com.example.demo.entity.authEntity;

import jakarta.persistence.*;
import lombok.*;
//import org.springframework.data.annotation.Id;
//import jakarta.persistence.Id;

import java.util.Date;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlackListToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String token;

    private Date expiryDate;

    public BlackListToken(String token, Date expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
