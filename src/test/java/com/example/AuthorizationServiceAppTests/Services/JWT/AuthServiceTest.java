package com.example.AuthorizationServiceAppTests.Services.JWT;

import com.example.AuthorizationServiceApp.AuthorizationServiceApp;
import com.example.AuthorizationServiceApp.Dto.UserDto;
import com.example.AuthorizationServiceApp.Entities.UserEntity;
import com.example.AuthorizationServiceApp.Exceptions.UserAlreadyExistsException;
import com.example.AuthorizationServiceApp.Repositories.UserRepository;
import com.example.AuthorizationServiceApp.Services.JWT.AuthService;
import com.example.AuthorizationServiceApp.Services.JWT.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthService authService;

	@BeforeEach
	void setUp() {
		UserEntity admin = new UserEntity();
		admin.setId(10L);
		admin.setUsername("admin");
		admin.setPassword("admin");
		admin.setEmail("admin@admin.com");
		admin.setRole("ROLE_ADMIN");

		when(userRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(new UserEntity()));
	}

	@Test
	void register_UserAlreadyExists() {
		UserDto userDto = new UserDto();
		userDto.setEmail("admin@admin.com");
		userDto.setPassword("admin");
		userDto.setUsername("admin");

		assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(userDto));
		verify(userRepository, never()).save(Mockito.any(UserEntity.class));
	}

}
