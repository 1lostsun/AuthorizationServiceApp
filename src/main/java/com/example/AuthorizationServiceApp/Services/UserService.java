package com.example.AuthorizationServiceApp.Services;

import com.example.AuthorizationServiceApp.Exceptions.IncorrectPasswordException;
import com.example.AuthorizationServiceApp.Exceptions.UserNotFoundException;
import com.example.AuthorizationServiceApp.Dto.UserDto;
import com.example.AuthorizationServiceApp.Entities.UserEntity;
import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import com.example.AuthorizationServiceApp.Services.JWT.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	public void registerUser(UserDto userDto) {
		if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
			throw new RuntimeException("Account with email " + userDto.getEmail() + " already exists");
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

	public UserDto getUserInfoFromJwt(String token) {
		String username = jwtUtil.extractUsernameFromToken(token);
		UserEntity userEntity = getUserByUsername(username);
		return new UserDto(userEntity.getEmail(), userEntity.getPassword(), userEntity.getUsername());
	}

	public UserEntity getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public UserEntity getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public void changePassword(Long id, String oldPassword, String newPassword) {
		UserEntity userEntity = getUserById(id);
		if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
			throw new IncorrectPasswordException("Incorrect password");
		}
		userEntity.setPassword(passwordEncoder.encode(newPassword));
	}

	public void changeEmail(Long id, String email) {
		UserEntity userEntity = getUserById(id);
		userEntity.setEmail(email);
	}

	public void updateUser(UserDto userDto) {
		UserEntity userEntity = getUserByUsername(userDto.getUsername());

		userEntity.setUsername(userDto.getUsername());
		userEntity.setEmail(userDto.getEmail());
		userRepository.save(userEntity);
	}

	public void deleteUserById(Long id) {
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException("User not found");
		}

		userRepository.deleteById(id);
	}

}
