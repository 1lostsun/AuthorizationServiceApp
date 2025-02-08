package com.example.paymentApp.Services;

import com.example.paymentApp.Configurations.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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

//	public boolean validateToken(String token) {
//
//	}

	public String getUsernameFromToken(String token) {
		try {
			Claims claim = (Claims) Jwts.parser().build().parseSignedClaims(token);
			return claim.getSubject();
		} catch (
				ExpiredJwtException e) {
			System.err.println("Token expired: " + e.getMessage());
		} catch (
				JwtException e) {
			System.err.println("Invalid token: " + e.getMessage());
		}
		return null;
	}

	public Claims extractClaimsFromToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().build().parseSignedClaims(token);
			return claimsJws.getPayload();
		} catch (
				ExpiredJwtException e)
		{
			System.err.println("Token expired, but returning claims: " + e.getMessage());
			return e.getClaims();
		} catch (
				JwtException e)
		{
			System.err.println("Invalid token: " + e.getMessage());
			return null;
		}
	}

}
