package com.example.AuthorizationServiceApp.Controllers;

import com.example.AuthorizationServiceApp.Dto.AuditMessageDto;
import com.example.AuthorizationServiceApp.Dto.AuthResponseDto;
import com.example.AuthorizationServiceApp.Dto.MessageDto;
import com.example.AuthorizationServiceApp.Dto.UserRequestDto;
import com.example.AuthorizationServiceApp.Services.JWT.AuthService;
import com.example.AuthorizationServiceApp.Services.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
	public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) throws JsonProcessingException {
		authService.registerUser(userRequestDto);
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(),"register" ,"User registered successfully with email: " + userRequestDto.getEmail(), "Welcome in our App"));
		kafkaProducer.sendMessage("audit-topic", null, new AuditMessageDto("register", userRequestDto.getUsername(), request.getRemoteAddr(), LocalDateTime.now().toString(), "User register successfully"));
		return ResponseEntity.status(201).body("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) throws JsonProcessingException {
		AuthResponseDto authResponseDto = authService.authenticateUser(userRequestDto.getEmail(), userRequestDto.getPassword());
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(), "login" ,"User logged in: " + userRequestDto.getEmail(), "You logged in"));
		kafkaProducer.sendMessage("audit-topic", null, new AuditMessageDto("login", userRequestDto.getUsername(), request.getRemoteAddr(), LocalDateTime.now().toString(), "User login successfully"));
		return ResponseEntity.ok(authResponseDto);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) throws JsonProcessingException {
		authService.logoutUser(userRequestDto.getUsername());
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(), "logout" ,"User logout successfully", "You logout successfully"));
		kafkaProducer.sendMessage("audit-topic", null, new AuditMessageDto("logout", userRequestDto.getUsername(), request.getRemoteAddr(), LocalDateTime.now().toString(), "User logout successfully"));
		return ResponseEntity.ok("User logout successfully");
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody UserRequestDto userRequestDto, HttpServletRequest request) throws JsonProcessingException {
		AuthResponseDto newTokens = authService.refreshToken(userRequestDto.getUsername());
		kafkaProducer.sendMessage("auth-topic", null, new MessageDto(userRequestDto.getEmail(), userRequestDto.getUsername(),"refresh", "User refreshed token", "You refreshed the token"));
		kafkaProducer.sendMessage("audit-topic", null, new AuditMessageDto("refresh token", userRequestDto.getUsername(), request.getRemoteAddr(), LocalDateTime.now().toString(), "User refreshed token"));
		return ResponseEntity.ok(newTokens);
	}
}
