package com.example.paymentApp.Controllers;

import com.example.paymentApp.Dto.UserDto;
import com.example.paymentApp.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerPage(@RequestBody UserDto userDto) {
		userService.registerUser(userDto);
		return ResponseEntity.ok("User register successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginPage(@RequestBody UserDto userDto) {
		String token = userService.authenticateUser(userDto.getEmail(), userDto.getPassword());
		return ResponseEntity.ok("User login successfully " + "with token " + token);
	}

	@GetMapping("/appPage")
	public String appPage() {
		return "appPage";
	}


}

