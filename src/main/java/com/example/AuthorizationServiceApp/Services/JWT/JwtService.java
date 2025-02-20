package com.example.AuthorizationServiceApp.Services.JWT;

import com.example.AuthorizationServiceApp.Configurations.AppConfig;
import com.example.AuthorizationServiceApp.Configurations.Redis.RedisConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtService {

	private final SecretKey key;
	private final int expirationTime;
	private final RedisTemplate<String, Object> redisTemplate;
	private final long redisExpirationTime;

	public JwtService(AppConfig appConfig, RedisTemplate<String, Object> redisTemplate, RedisConfig redisConfig) {
		this.key = Keys.hmacShaKeyFor(appConfig.getSecretKey().getBytes());
		this.expirationTime = appConfig.getExpirationTime();
		this.redisTemplate = redisTemplate;
		this.redisExpirationTime = redisConfig.getRedisTokenExpirationTime();
	}

	public String generateToken(String username) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationTime);

		String token =  Jwts
				.builder()
				.signWith(this.key)
				.subject(username)
				.issuedAt(now)
				.expiration(expiration)
				.compact();
		cacheToken(username, token, redisExpirationTime);
		return token;
	}

	public boolean validateToken(String token) {
		try {
			String usernameFromToken = extractUsernameFromToken(token);
			String cachedToken = (String) redisTemplate.opsForValue().get("jwt:" + usernameFromToken);

			return token.equals(cachedToken);
		} catch (Exception e) {
			return false;
		}

	}

	public void cacheToken(String username, String token, long expiration) {
		redisTemplate.opsForValue().set("jwt: " + username, token, Duration.ofSeconds(expiration));
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
		boolean expiration = extractExpirationDateFromToken(token).before(new Date());
		if (expiration) {
			throw new ExpiredJwtException(null, null, "Token has expired");
		}
		return false;
	}

	public void revokeToken(String username) {
		redisTemplate.delete(
				"jwt: " + username
		);
	}
}