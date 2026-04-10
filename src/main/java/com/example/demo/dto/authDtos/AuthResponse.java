package com.example.demo.dto.authDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {
	private Long UserId;
	private String accessToken;
    private String refreshToken;
    private String email;
    private String role;
}
