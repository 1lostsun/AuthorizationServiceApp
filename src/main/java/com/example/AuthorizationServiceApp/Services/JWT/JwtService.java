package com.example.AuthorizationServiceApp.Services.JWT;

import com.example.AuthorizationServiceApp.Configurations.AppConfig;
import com.example.AuthorizationServiceApp.Services.Redis.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {

	private final SecretKey key;
	private final int accessTokenExpirationTime;
	private final int refreshTokenExpirationTime;
	private final RefreshTokenService refreshTokenService;

	public JwtService(AppConfig appConfig, RefreshTokenService refreshTokenService) {
		this.key = Keys.hmacShaKeyFor(appConfig.getSecretKey().getBytes());
		this.accessTokenExpirationTime = appConfig.getAccessTokenExpirationTime();
		this.refreshTokenExpirationTime = appConfig.getRefreshTokenExpirationTime();
		this.refreshTokenService = refreshTokenService;
	}

	public String generateAccessToken(String username) {
		return generateToken(username, accessTokenExpirationTime, "access");
	}

	public String generateRefreshToken(String username) {
		String refreshToken = generateToken(username, refreshTokenExpirationTime, "refresh");
		refreshTokenService.saveRefreshToken(username, refreshToken);
		return refreshToken;
	}

	private String generateToken(String username, int refreshTokenExpirationTime, String type) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + refreshTokenExpirationTime);

		return Jwts
				.builder()
				.signWith(this.key)
				.subject(username)
				.issuedAt(now)
				.expiration(expiration)
				.claim("type", type)
				.compact();
	}

	public boolean validateToken(String token, String type) {
		if (token == null) {
			return false;
		}

		try {
			if (type.equals("access")) {
				return isTokenExpired(token);
			} else if (type.equals("refresh")) {
				String username = extractUsernameFromToken(token);
				if (username == null) return false;

				String storedToken = refreshTokenService.getRefreshToken(username);
				return storedToken != null && storedToken.equals(token);
			}
		} catch (Exception e) {
			return false;
		}

		return false;
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
		try {
			return !extractExpirationDateFromToken(token).before(new Date());
		} catch (ExpiredJwtException e) {
			return false;
		}
	}
	public String getRefreshToken(String username) {
		return refreshTokenService.getRefreshToken(username);
	}

	public void revokeRefreshToken(String username) {
		refreshTokenService.deleteRefreshToken(username);
	}
}