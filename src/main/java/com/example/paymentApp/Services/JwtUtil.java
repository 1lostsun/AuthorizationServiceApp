package com.example.paymentApp.Services;

import com.example.paymentApp.Configurations.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
			String username = userDetails.getUsername();
			String usernameFromToken = extractUsernameFromToken(token);

			if (!username.equals(usernameFromToken)) {
				throw new IllegalArgumentException("Invalid username in token");
			}

			if (isTokenExpired(token)) {
				throw new ExpiredJwtException(null, null, "Token has expired");
			}

			return true;
	}

	public String refreshToken(String token) {
		try {
			if (isTokenExpired(token)) {
				Claims claims = extractClaimsFromToken(token);
				return generateToken(claims.getSubject());
			}
			return token;
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