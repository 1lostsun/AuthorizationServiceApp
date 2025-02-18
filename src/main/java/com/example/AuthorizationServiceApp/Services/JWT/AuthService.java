package com.example.AuthorizationServiceApp.Services.JWT;

import com.example.AuthorizationServiceApp.Dto.UserDto;
import com.example.AuthorizationServiceApp.Entities.UserEntity;
import com.example.AuthorizationServiceApp.Exceptions.IncorrectPasswordException;
import com.example.AuthorizationServiceApp.Exceptions.UserAlreadyExistsException;
import com.example.AuthorizationServiceApp.Exceptions.UserNotFoundException;
import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	public void registerUser(UserDto userDto) {
		if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("Account with email " + userDto.getEmail() + " already exists");
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userDto.getEmail());
		userEntity.setUsername(userDto.getUsername());
		userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userEntity.setRole("USER");
		userRepository.save(userEntity);
	}

	public String authenticateUser(String email, String password) {
		UserEntity userEntity = userRepository
				.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		if (!passwordEncoder.matches(password, userEntity.getPassword())) {
			throw new IncorrectPasswordException("Incorrect password");
		}

		return jwtUtil.generateToken(userEntity.getUsername());
	}

}
