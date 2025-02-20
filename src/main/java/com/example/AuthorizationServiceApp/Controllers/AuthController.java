package com.example.AuthorizationServiceApp.Controllers;

import com.example.AuthorizationServiceApp.Dto.UserDto;
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
	public ResponseEntity<?> register(@RequestBody UserDto userDto, RedirectAttributes redirectAttributes) {
		authService.registerUser(userDto);
		redirectAttributes.addFlashAttribute("message", "User registered successfully");
		return ResponseEntity.status(201).body("redirect:/auth");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {
		String token = authService.authenticateUser(userDto.getEmail(), userDto.getPassword());
		kafkaProducer.sendMessage("auth-topic", null, "User login successfully");
		return ResponseEntity.ok("User login successfully " + "with token " + token);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody UserDto userDto) {
		kafkaProducer.sendMessage("auth-topic", null, "User logout successfully");
		authService.logoutUser(userDto.getUsername());
		return ResponseEntity.ok("User logout successfully");
	}

}
