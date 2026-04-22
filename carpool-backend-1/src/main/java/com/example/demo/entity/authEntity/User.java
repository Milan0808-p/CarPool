package com.example.demo.entity.authEntity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "public_id", unique = true, nullable = false, updatable = false)
    protected String publicId;
	
	private String email;

	private String username;

	private String phone;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role role; // USER, DRIVER, BOTH
	
	@PrePersist
    protected void onCreate() {
        this.publicId = UUID.randomUUID().toString();
    }

}
