package com.example.AuthorizationServiceAppTests.Services.JWT;

import com.example.AuthorizationServiceApp.AuthorizationServiceApp;
import com.example.AuthorizationServiceApp.Dto.UserDto;
import com.example.AuthorizationServiceApp.Entities.UserEntity;
import com.example.AuthorizationServiceApp.Exceptions.IncorrectPasswordException;
import com.example.AuthorizationServiceApp.Exceptions.UserAlreadyExistsException;
import com.example.AuthorizationServiceApp.Exceptions.UserNotFoundException;
import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import com.example.AuthorizationServiceApp.Services.JWT.AuthService;
import com.example.AuthorizationServiceApp.Services.JWT.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AuthorizationServiceApp.class)
public class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private AuthService authService;

	@Test
	void register_UserAlreadyExists() {
		UserDto userDto = new UserDto();
		userDto.setEmail("admin@admin.com");
		userDto.setPassword("admin");
		userDto.setUsername("admin");

		UserEntity admin = new UserEntity();
		admin.setEmail("admin@admin.com");

		when(userRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));

		assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(userDto));
		verify(userRepository, never()).save(any(UserEntity.class));
	}

	@Test
	void registerUserTest() {
		UserDto userDto = new UserDto();
		userDto.setEmail("user@user.com");
		userDto.setPassword("user");
		userDto.setUsername("user");

		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userDto.getEmail());
		userEntity.setUsername(userDto.getUsername());

		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));

		authService.registerUser(userDto);
		assertTrue(userRepository.findByUsername(userDto.getUsername()).isPresent());
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}

	@Test
	void authenticateUserTest_IncorrectPassword() {

		UserDto userDto = new UserDto();
		userDto.setEmail("user@user.com");
		userDto.setPassword("incorrectPassword");

		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userDto.getEmail());
		userEntity.setPassword("user");

		when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(userEntity));
		when(passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())).thenReturn(false);
		assertThrows(IncorrectPasswordException.class, () -> authService.authenticateUser(userDto.getEmail(), userDto.getPassword()));

	}

	@Test
	void authenticateUserTest_UserNotFound() {
		UserDto userDto = new UserDto();
		userDto.setEmail("user@user.com");
		userDto.setPassword("user");

		when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
		assertThrows(UserNotFoundException.class, () -> authService.authenticateUser(userDto.getEmail(), userDto.getPassword()));
	}

	@Test
	void authenticateUserTest() {
		String expectedToken = "jwt_token";
		UserDto userDto = new UserDto();
		userDto.setEmail("user@user.com");
		userDto.setPassword("user");
		userDto.setUsername("user");

		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userDto.getEmail());
		userEntity.setPassword("encoded password");

		when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(userEntity));
		when(passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())).thenReturn(true);
		when(jwtService.generateToken(any())).thenReturn(expectedToken);

		String token = authService.authenticateUser(userDto.getEmail(), userDto.getPassword());

		assertEquals(expectedToken, token);
	}

}
