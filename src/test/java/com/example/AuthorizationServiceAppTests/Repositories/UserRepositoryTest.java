//package com.example.AuthorizationServiceAppTests.Repositories;
//
//import com.example.AuthorizationServiceApp.AuthorizationServiceApp;
//import com.example.AuthorizationServiceApp.Entities.UserEntity;
//import com.example.AuthorizationServiceApp.Repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest(classes = AuthorizationServiceApp.class)
//@Transactional
//public class UserRepositoryTest {
//
//	@Autowired
//	private UserRepository userRepository;
//	private UserEntity admin;
//
//
//	@BeforeEach
//	void setUp() {
//		admin = new UserEntity();
//		admin.setUsername("admin");
//		admin.setPassword("admin");
//		admin.setEmail("admin@admin.com");
//		admin.setRole("ROLE_ADMIN");
//		userRepository.save(admin);
//	}
//
//	@Test
//	void findUserByUsername() {
//
//		Optional<UserEntity> user = userRepository.findByUsername(admin.getUsername());
//
//		assertTrue(user.isPresent());
//		assertEquals(admin.getUsername(), user.get().getUsername());
//	}
//
//	@Test
//	void findUserByEmail() {
//		Optional<UserEntity> user = userRepository.findByEmail(admin.getEmail());
//		assertTrue(user.isPresent());
//		assertEquals(admin.getEmail(), user.get().getEmail());
//	}
//}
