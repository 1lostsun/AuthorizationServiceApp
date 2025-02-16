package com.example.AuthorizationServiceApp.Services;

import com.example.AuthorizationServiceApp.Exceptions.IncorrectPasswordException;
import com.example.AuthorizationServiceApp.Exceptions.UserNotFoundException;
import com.example.AuthorizationServiceApp.Dto.UserDto;
import com.example.AuthorizationServiceApp.Entities.UserEntity;
import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import com.example.AuthorizationServiceApp.Services.JWT.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

	public void changePassword(String username, String oldPassword, String newPassword) {
		UserEntity userEntity = getUserByUsername(username);
		if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
			throw new IncorrectPasswordException("Incorrect password");
		}
		userEntity.setPassword(passwordEncoder.encode(newPassword));
	}

	public void changeEmail(String username, String email, String newEmail) {
		UserEntity userEntity = getUserByUsername(username);
		userEntity.setEmail(newEmail);
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
