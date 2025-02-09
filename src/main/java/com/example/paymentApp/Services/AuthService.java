package com.example.paymentApp.Services;

import com.example.paymentApp.Entities.UserEntity;
import com.example.paymentApp.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

}
