package com.example.demo.dto.authDtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequest {
	@Column(nullable = false)
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@Column(nullable = false)
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
}
