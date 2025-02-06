package com.example.paymentApp.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/appPage")
	public String appPage() {
		return "appPage";
	}
}

