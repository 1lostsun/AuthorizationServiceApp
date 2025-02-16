package com.example.AuthorizationServiceAppTests.Services;

import com.example.AuthorizationServiceApp.AuthorizationServiceApp;
import com.example.AuthorizationServiceApp.Entities.UserEntity;
import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import com.example.AuthorizationServiceApp.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AuthorizationServiceApp.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		UserEntity admin = new UserEntity();
		admin.setId(10L);
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.setEmail("admin@admin.com");
		admin.setRole("ROLE_ADMIN");

		when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
		when(userRepository.findById(10L)).thenReturn(Optional.of(admin));
	}

	@Test
	void getUserByUsernameTest() {
		UserEntity user = userService.getUserByUsername("admin");

		assertEquals("admin", user.getUsername());
	}

	@Test
	void getUserByIdTest() {
		UserEntity user = userService.getUserById(10L);
		assertEquals("admin", user.getUsername());
	}

	@Test
	void changeEmailTest() {
		String newEmail = "Admin@admin.com";
		UserEntity user = userService.getUserByUsername("admin");
		userService.changeEmail(user.getUsername(), user.getEmail(), newEmail);
		assertEquals("Admin@admin.com", user.getEmail());
	}

	@Test
	void changePasswordTest() {
		String oldPassword = "admin";
		String newPassword = "Admin";
		UserEntity user = userService.getUserByUsername("admin");

		when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
		when(passwordEncoder.encode(newPassword)).thenReturn("$2a$10$newHashedPassword");

		userService.changePassword(user.getUsername(), oldPassword, newPassword);
		assertEquals("$2a$10$newHashedPassword", user.getPassword());
	}

}
