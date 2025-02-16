package com.example.AuthorizationServiceApp.Controllers;

import com.example.AuthorizationServiceApp.Dto.UserDto;
import com.example.AuthorizationServiceApp.Services.Kafka.KafkaProducer;
import com.example.AuthorizationServiceApp.Services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final UserService userService;
	private final KafkaProducer kafkaProducer;

	public UserController(UserService userService, KafkaProducer kafkaProducer) {
		this.userService = userService;
		this.kafkaProducer = kafkaProducer;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDto userDto) {
		try {
			userService.registerUser(userDto);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok("User register successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {
		String token = userService.authenticateUser(userDto.getEmail(), userDto.getPassword());
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

}

