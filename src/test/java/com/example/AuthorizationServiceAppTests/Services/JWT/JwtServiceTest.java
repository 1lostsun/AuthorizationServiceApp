//package com.example.AuthorizationServiceAppTests.Services.JWT;
//
//import com.example.AuthorizationServiceApp.AuthorizationServiceApp;
//import com.example.AuthorizationServiceApp.Configurations.AppConfig;
//import com.example.AuthorizationServiceApp.Services.JWT.JwtService;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = AuthorizationServiceApp.class)
//@ExtendWith(MockitoExtension.class)
//public class JwtServiceTest {
//
//	@Mock
//	private AppConfig appConfig;
//
//	private JwtService jwtService;
//	private String expiredToken;
//	private String nonExpiredToken;
//
//	@BeforeEach
//	void setUp() {
//		when(appConfig.getSecretKey()).thenReturn("this_is_a_very_secure_secret_key_which_is_256_bits!");
//		jwtService = new JwtService(appConfig);
//		expiredToken = Jwts
//				.builder()
//				.subject("Username")
//				.issuedAt(new Date(System.currentTimeMillis()))
//				.expiration(new Date(System.currentTimeMillis() - 1000000))
//				.signWith(Keys.hmacShaKeyFor(appConfig.getSecretKey().getBytes()))
//				.compact();
//
//		nonExpiredToken = Jwts
//				.builder()
//				.subject("Username")
//				.issuedAt(new Date())
//				.expiration(new Date(System.currentTimeMillis() + 1000000))
//				.signWith(Keys.hmacShaKeyFor(appConfig.getSecretKey().getBytes()))
//				.compact();
//	}
//
//	@Test
//	void isTokenExpiredTest_TokenExpired() {
//		assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenExpired(expiredToken));
//	}
//
//	@Test
//	void isTokenExpiredTest_TokenNotExpired() {
//		boolean expired = jwtService.isTokenExpired(nonExpiredToken);
//		assertFalse(expired);
//	}
//
//	@Test
//	void extractUsernameFromTokenTest() {
//		String expectedUsername = "Username";
//		assertEquals(expectedUsername, jwtService.extractUsernameFromToken(nonExpiredToken));
//	}
//
//}
