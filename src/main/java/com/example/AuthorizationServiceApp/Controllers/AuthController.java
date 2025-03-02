package com.example.AuthorizationServiceApp.Controllers;

import com.example.AuthorizationServiceApp.Dto.AuthResponseDto;
import com.example.AuthorizationServiceApp.Dto.UserRequestDto;
import com.example.AuthorizationServiceApp.Services.JWT.AuthService;
import com.example.AuthorizationServiceApp.Services.Kafka.KafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController()
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final KafkaProducer kafkaProducer;

	public AuthController(AuthService authService, KafkaProducer kafkaProducer) {
		this.authService = authService;
		this.kafkaProducer = kafkaProducer;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto, RedirectAttributes redirectAttributes) {
		authService.registerUser(userRequestDto);
		kafkaProducer.sendMessage("auth-topic", null, "User registered successfully");
		return ResponseEntity.status(201).body("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
		AuthResponseDto authResponseDto = authService.authenticateUser(userRequestDto.getEmail(), userRequestDto.getPassword());
		kafkaProducer.sendMessage("auth-topic", null, "User logged in: " + userRequestDto.getEmail() );
		return ResponseEntity.ok(authResponseDto);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody UserRequestDto userRequestDto) {
		authService.logoutUser(userRequestDto.getUsername());
		kafkaProducer.sendMessage("auth-topic", null, "User logout successfully");
		return ResponseEntity.ok("User logout successfully");
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody UserRequestDto userRequestDto) {
		AuthResponseDto newTokens = authService.refreshToken(userRequestDto.getUsername());
		kafkaProducer.sendMessage("auth-topic", null, "User refreshed token");
		return ResponseEntity.ok(newTokens);
	}
}
