package com.example.AuthorizationServiceApp.Controllers;

import com.example.AuthorizationServiceApp.Dto.UserRequestDto;
import com.example.AuthorizationServiceApp.Services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/appPage")
	public ResponseEntity<String> appPage() {
		return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("<h1>appPage<h1>");
	}

	@GetMapping("some/page")
	public ResponseEntity<String> somePage() {
		return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("<h1>some page<h1>");
	}

	@GetMapping("/homePage")
	public ResponseEntity<String> homePage() {
		return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("<h1>Server is running!<h1>");
	}

	@PatchMapping("/change/email")
	public ResponseEntity<?> changeEmail(@RequestBody UserRequestDto userRequestDto, @RequestParam String newEmail) {
		userService.changeEmail(userRequestDto.getUsername(), newEmail);
		return ResponseEntity.ok("Email changed successfully: " + newEmail);
	}

	@PatchMapping("/change/password")
	public ResponseEntity<?> changePassword(@RequestBody UserRequestDto userRequestDto, @RequestParam String newPassword) {
		userService.changePassword(userRequestDto.getUsername(), userRequestDto.getPassword(), newPassword);
		return ResponseEntity.ok("Password changed successfully: " + newPassword);
	}

}

