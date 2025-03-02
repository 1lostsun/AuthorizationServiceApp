package com.example.AuthorizationServiceApp.Services.JWT;

import com.example.AuthorizationServiceApp.Dto.AuthResponseDto;
import com.example.AuthorizationServiceApp.Dto.UserRequestDto;
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
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public void registerUser(UserRequestDto userRequestDto) {
		if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("Account with email " + userRequestDto.getEmail() + " already exists");
		}

		if (userRequestDto.getPassword().length() < 6) {
			throw new IncorrectPasswordException("Password must be at least 6 characters");
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userRequestDto.getEmail());
		userEntity.setUsername(userRequestDto.getUsername());
		userEntity.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
		userEntity.setRole("USER");
		userRepository.save(userEntity);
	}

	public AuthResponseDto authenticateUser(String email, String password) {
		UserEntity userEntity = userRepository
				.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found"));

		if (!passwordEncoder.matches(password, userEntity.getPassword())) {
			throw new IncorrectPasswordException("Incorrect password");
		}

		String accessToken = jwtService.generateAccessToken(userEntity.getUsername());
		String refreshToken = jwtService.generateRefreshToken(userEntity.getUsername());

		return new AuthResponseDto(accessToken, refreshToken);
	}

	public AuthResponseDto refreshToken(String username) {
		String refreshToken = jwtService.getRefreshToken(username);
		if (!jwtService.validateToken(refreshToken, "refresh")) {
			throw new RuntimeException("Invalid refresh token");
		}

		String newAccessToken = jwtService.generateAccessToken(username);

		return new AuthResponseDto(newAccessToken, refreshToken);
	}

	public void logoutUser(String username) {

		jwtService.revokeRefreshToken(username);
	}

}
