package com.example.paymentApp.Services;

import com.example.paymentApp.Dto.UserDto;
import com.example.paymentApp.Entities.UserEntity;
import com.example.paymentApp.Repositories.UserRepository;
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
			throw new RuntimeException("Email already exists");
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
				.orElseThrow(() -> new RuntimeException("User not found"));
		if (!passwordEncoder.matches(password, userEntity.getPassword())) {
			throw new RuntimeException("Incorrect password");
		}

		return jwtUtil.generateToken(userEntity.getUsername());
	}

	public UserEntity getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public UserEntity getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	public void changePassword(Long id, String oldPassword, String newPassword) {
		UserEntity userEntity = getUserById(id);
		if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
			throw new RuntimeException("Incorrect password");
		}
		userEntity.setPassword(passwordEncoder.encode(newPassword));
	}

	public void changeEmail(Long id, String email) {
		UserEntity userEntity = getUserById(id);
		userEntity.setEmail(email);
	}

	public void updateUser(UserDto userDto) {
		UserEntity userEntity = getUserByEmail(userDto.getEmail());

		userEntity.setUsername(userDto.getUsername());
		userEntity.setEmail(userDto.getEmail());
		userRepository.save(userEntity);
	}

	public void updateRole(Long id, String role) {
		UserEntity userEntity = getUserById(id);

		userEntity.setRole(role);
	}

	public void deleteUserById(Long id) {
		if (!userRepository.existsById(id)) {
			throw new RuntimeException("User not found");
		}

		userRepository.deleteById(id);
	}

}
