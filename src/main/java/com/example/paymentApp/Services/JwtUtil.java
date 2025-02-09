package com.example.paymentApp.Services;

import com.example.paymentApp.Configurations.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	private static AppConfig appConfig;

	public String generateToken(String username) {

		Key key = Keys.hmacShaKeyFor(appConfig.getSecretKey().getBytes());
		Date now = new Date();
		Date expiration = new Date(now.getTime() + appConfig.getExpirationTime());

		return Jwts
				.builder()
				.subject(username)
				.issuedAt(now)
				.expiration(expiration)
				.compact();

	}

	public boolean validateToken(String token, UserDetails userDetails) {
		String username = userDetails.getUsername();
		String usernameFromToken = extractUsernameFromToken(token);

		return (username.equals(usernameFromToken) && !isTokenExpired(token));
	}

	public String extractUsernameFromToken(String token) {
		Claims claim = extractClaimsFromToken(token);
		return claim.getSubject();
	}

	public Claims extractClaimsFromToken(String token) {
			Jws<Claims> claimsJws = Jwts.parser().build().parseSignedClaims(token);
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
