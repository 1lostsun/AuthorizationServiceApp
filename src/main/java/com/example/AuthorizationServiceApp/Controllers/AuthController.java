package com.example.AuthorizationServiceApp.Controllers;

import com.example.AuthorizationServiceApp.Dto.AuthResponseDto;
import com.example.AuthorizationServiceApp.Dto.MessageDto;
import com.example.AuthorizationServiceApp.Dto.UserRequestDto;
import com.example.AuthorizationServiceApp.Services.JWT.AuthService;
import com.example.AuthorizationServiceApp.Services.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
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
	public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto, RedirectAttributes redirectAttributes) throws JsonProcessingException {
		authService.registerUser(userRequestDto);
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(),"User registered successfully with email: " + userRequestDto.getEmail(), "Welcome in our App"));
		return ResponseEntity.status(201).body("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) throws JsonProcessingException {
		AuthResponseDto authResponseDto = authService.authenticateUser(userRequestDto.getEmail(), userRequestDto.getPassword());
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(), "User logged in: " + userRequestDto.getEmail(), "You logged in"));
		return ResponseEntity.ok(authResponseDto);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody UserRequestDto userRequestDto) throws JsonProcessingException {
		authService.logoutUser(userRequestDto.getUsername());
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(),"User logout successfully", "You logout successfully"));
		return ResponseEntity.ok("User logout successfully");
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody UserRequestDto userRequestDto) throws JsonProcessingException {
		AuthResponseDto newTokens = authService.refreshToken(userRequestDto.getUsername());
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(),"User refreshed token", "Thanks for staying with us"));
		return ResponseEntity.ok(newTokens);
	}
}
