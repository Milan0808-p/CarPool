package com.example.demo.dto.authDtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


//@Entity
//@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
	
//		@Id
//		@GeneratedValue(strategy = GenerationType.IDENTITY)
//		private Long id;
		
	@Column(nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "username")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be 3-20 characters")
    private String username;
    
    @NotBlank @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number") 
    private String phone;
    
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "USER|DRIVER", message = "Role must be USER or DRIVER")
    private String role;

	
}
