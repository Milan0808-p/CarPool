package com.example.demo.entity.authEntity;

import com.example.demo.dto.authDtos.UserDto;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String email;
		
		private String username;
		
		private String phone;
		
		private String password;
		
		@Enumerated(EnumType.STRING)
	    private Role role; // USER, DRIVER, BOTH

	
}
