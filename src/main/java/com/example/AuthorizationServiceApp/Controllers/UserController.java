package com.example.AuthorizationServiceApp.Controllers;

import com.example.AuthorizationServiceApp.Dto.UserDto;
import com.example.AuthorizationServiceApp.Services.JWT.AuthService;
import com.example.AuthorizationServiceApp.Services.Kafka.KafkaProducer;
import com.example.AuthorizationServiceApp.Services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final UserService userService;
	private final AuthService authService;
	private final KafkaProducer kafkaProducer;

	public UserController(UserService userService, AuthService authService, KafkaProducer kafkaProducer) {
		this.userService = userService;
		this.authService = authService;
		this.kafkaProducer = kafkaProducer;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDto userDto) {
		try {
			authService.registerUser(userDto);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok("User register successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {
		String token = authService.authenticateUser(userDto.getEmail(), userDto.getPassword());
		kafkaProducer.sendMessage("auth-topic", null, "User login successfully");
		return ResponseEntity.ok("User login successfully " + "with token " + token);
	}

	@GetMapping("/appPage")
	public String appPage() {
		return "appPage";
	}

	@GetMapping("/homePage")
	public ResponseEntity<String> homePage() {
		return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("<h1>Server is running!<h1>");
	}

	@PatchMapping("/change/email")
	public ResponseEntity<?> changeEmail(@RequestBody UserDto userDto, @RequestParam String newEmail) {
		userService.changeEmail(userDto.getUsername(), userDto.getEmail(), newEmail);
		return ResponseEntity.ok("Email changed successfully: " + newEmail);
	}

	@PatchMapping("/change/password")
	public ResponseEntity<?> changePassword(@RequestBody UserDto userDto, @RequestParam String newPassword) {
		userService.changePassword(userDto.getUsername(), userDto.getPassword(), newPassword);
		return ResponseEntity.ok("Password changed successfully: " + newPassword);
	}

}

