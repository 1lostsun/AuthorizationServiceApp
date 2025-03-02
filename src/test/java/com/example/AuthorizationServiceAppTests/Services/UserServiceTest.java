//package com.example.AuthorizationServiceAppTests.Services;
//
//import com.example.AuthorizationServiceApp.AuthorizationServiceApp;
//import com.example.AuthorizationServiceApp.Dto.UserDto;
//import com.example.AuthorizationServiceApp.Entities.UserEntity;
//import com.example.AuthorizationServiceApp.Exceptions.IncorrectPasswordException;
//import com.example.AuthorizationServiceApp.Exceptions.UserNotFoundException;
//import com.example.AuthorizationServiceApp.Repositories.UserRepository;
//import com.example.AuthorizationServiceApp.Services.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = AuthorizationServiceApp.class)
//public class UserServiceTest {
//
//	@Mock
//	private UserRepository userRepository;
//
//	@Mock
//	private PasswordEncoder passwordEncoder;
//
//	@InjectMocks
//	private UserService userService;
//
//	@BeforeEach
//	void setUp() {
//		UserEntity admin = new UserEntity();
//		admin.setId(10L);
//		admin.setUsername("admin");
//		admin.setPassword("admin");
//		admin.setEmail("admin@admin.com");
//		admin.setRole("ROLE_ADMIN");
//
//		when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
//		when(userRepository.findById(10L)).thenReturn(Optional.of(admin));
//		when(userRepository.existsById(10L)).thenReturn(true);
//		when(userRepository.existsById(1L)).thenReturn(false);
//		when(userRepository.findById(1L)).thenReturn(Optional.empty());
//		when(userRepository.findByUsername("nonAdmin")).thenReturn(Optional.empty());
//	}
//
//	@Test
//	void getUserByUsernameTest_UserFoundSuccessfully() {
//		UserEntity user = userService.getUserByUsername("admin");
//
//		assertEquals("admin", user.getUsername());
//	}
//
//	@Test
//	void getUserByUsernameTest_UserNotFound() {
//		assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nonAdmin"));
//		verify(userRepository, times(1)).findByUsername("nonAdmin");
//	}
//
//	@Test
//	void getUserByIdTest_UserFoundSuccessfully() {
//		UserEntity user = userService.getUserById(10L);
//		assertEquals("admin", user.getUsername());
//	}
//
//	@Test
//	void getUserByIdTest_UserNotFound() {
//		assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
//		verify(userRepository, times(1)).findById(1L);
//	}
//
//	@Test
//	void changeEmailTest() {
//		String newEmail = "Admin@admin.com";
//		UserEntity user = userService.getUserByUsername("admin");
//		userService.changeEmail(user.getUsername(), newEmail);
//		assertEquals(newEmail, user.getEmail());
//	}
//
//	@Test
//	void changePasswordTest() {
//		String oldPassword = "admin";
//		String newPassword = "Admin";
//		UserEntity user = userService.getUserByUsername("admin");
//
//		when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
//		when(passwordEncoder.encode(newPassword)).thenReturn("$2a$10$newHashedPassword");
//
//		userService.changePassword(user.getUsername(), oldPassword, newPassword);
//		assertEquals("$2a$10$newHashedPassword", user.getPassword());
//	}
//
//	@Test
//	void changePasswordTest_IncorrectPassword() {
//		String newPassword = "Admin";
//		UserDto userDto = new UserDto();
//		userDto.setUsername("admin");
//		userDto.setPassword("Admin");
//
//		UserEntity user = userService.getUserByUsername("admin");
//
//		when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(false);
//		assertThrows(IncorrectPasswordException.class, () -> userService.changePassword(userDto.getUsername(), userDto.getPassword(), newPassword));
//		verify(userRepository, never()).save(any(UserEntity.class));
//	}
//
//	@Test
//	void updateUserTest() {
//		UserDto userDto = new UserDto();
//		userDto.setUsername("admin");
//		userDto.setEmail("newEmail@admin.com");
//		userDto.setPassword("admin");
//
//		UserEntity user = userService.getUserByUsername("admin");
//		userService.updateUser(userDto);
//		assertEquals("admin", user.getUsername());
//		assertEquals("newEmail@admin.com", user.getEmail());
//	}
//
//	@Test
//	void deleteUserTest_UserExisting() {
//		userService.deleteUserById(10L);
//		verify(userRepository, times(1)).deleteById(10L);
//	}
//
//	@Test
//	void deleteUserTest_UserNotExisting() {
//		assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
//		verify(userRepository, never()).deleteById(anyLong());
//	}
//
//}
