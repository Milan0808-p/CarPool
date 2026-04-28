package com.example.demo.dto.authDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthResponseDTO {
	private String userId;
	private String accessToken;
    private String refreshToken;
    private String email;
    private String role;
}
