package com.example.paymentApp.Services;

import com.example.paymentApp.Configurations.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

	private final SecretKey key;
	private final int expirationTime;

	public JwtUtil(AppConfig appConfig) {
		this.key = Keys.hmacShaKeyFor(appConfig.getSecretKey().getBytes());
		this.expirationTime = appConfig.getExpirationTime();
	}

	public String generateToken(String username) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationTime);
		return Jwts
				.builder()
				.signWith(this.key)
				.subject(username)
				.issuedAt(now)
				.expiration(expiration)
				.compact();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		try {
			String username = userDetails.getUsername();
			String usernameFromToken = extractUsernameFromToken(token);
			return (username.equals(usernameFromToken) && !isTokenExpired(token));
		} catch (ExpiredJwtException e) {
			System.err.println("Токен истек: " + e.getMessage());
			return false;
		} catch (Exception e) {
			System.err.println("Ошибка при проверке токена: " + e.getMessage());
			return false;
		}
	}

	public String refreshToken(String token) {
		try {
			if (isTokenExpired(token)) {
				String username = extractUsernameFromToken(token);
				return generateToken(username);
			}
			return token;
		} catch (ExpiredJwtException e) {
			System.err.println("Токен истек: " + e.getMessage());
			String username = e.getClaims().getSubject();
			return generateToken(username);
		} catch (Exception e) {
			System.err.println("Ошибка при обновлении токена: " + e.getMessage());
			return null;
		}
	}

	public String extractUsernameFromToken(String token) {
		Claims claim = extractClaimsFromToken(token);
		return claim.getSubject();
	}

	public Claims extractClaimsFromToken(String token) {
		System.out.println(token);
		Jws<Claims> claimsJws = Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token);
		return claimsJws.getPayload();
	}

	public Date extractExpirationDateFromToken(String token) {
		Claims claims = extractClaimsFromToken(token);
		return claims.getExpiration();
	}

	public boolean isTokenExpired(String token) {
		return extractExpirationDateFromToken(token).before(new Date());
	}
}