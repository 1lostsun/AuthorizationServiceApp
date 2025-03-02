package com.example.AuthorizationServiceApp.Services;

import com.example.AuthorizationServiceApp.Exceptions.IncorrectPasswordException;
import com.example.AuthorizationServiceApp.Exceptions.UserNotFoundException;
import com.example.AuthorizationServiceApp.Dto.UserRequestDto;
import com.example.AuthorizationServiceApp.Entities.UserEntity;
import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
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

	public void changeEmail(String username, String newEmail) {
		UserEntity userEntity = getUserByUsername(username);
		userEntity.setEmail(newEmail);
	}

	public void updateUser(UserRequestDto userRequestDto) {
		UserEntity userEntity = getUserByUsername(userRequestDto.getUsername());

		userEntity.setUsername(userRequestDto.getUsername());
		userEntity.setEmail(userRequestDto.getEmail());
		userRepository.save(userEntity);
	}

	public void deleteUserById(Long id) {
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException("User not found");
		}

		userRepository.deleteById(id);
	}

}
