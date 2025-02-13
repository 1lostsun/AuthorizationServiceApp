package com.example.paymentApp.Controllers;

import com.example.paymentApp.Dto.UserDto;
import com.example.paymentApp.Services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
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

//	@PostMapping("/user/info")
//	public ResponseEntity<?> userInfo(@RequestHeader("Authorization") String token) {
//		try {
//			return ResponseEntity.ok().body(userService.getUserInfoFromJwt(token.substring(7)));
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}

}

