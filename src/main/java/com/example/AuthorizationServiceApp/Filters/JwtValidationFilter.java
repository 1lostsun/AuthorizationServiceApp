package com.example.AuthorizationServiceApp.Filters;

import com.example.AuthorizationServiceApp.Services.JWT.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

	private final RedisTemplate<String, String> redisTemplate;
	private final JwtService jwtService;

	public JwtValidationFilter(RedisTemplate<String, String> redisTemplate, JwtService jwtService) {
		this.redisTemplate = redisTemplate;
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String authToken = extractToken(request);

		if (isTokenValid(authToken)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token invalid or expired");
			return;
		}

		filterChain.doFilter(request, response);
	}

	public String extractToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	public boolean isTokenValid(String token) {
		if (token == null) {
			logger.info("No token provided, skipping blacklist check.");
			return false;
		}

		try {
			String username = jwtService.extractUsernameFromToken(token);
			if (username == null) {
				logger.warn("Failed to extract username from token.");
				return true;
			}

			String redisKey = "jwt:" + username;
			String storedToken = redisTemplate.opsForValue().get(redisKey);

			if (storedToken == null) {
				logger.info("Token not found in Redis, assuming valid.");
				return false;
			}

			if (!storedToken.equals(token)) {
				logger.warn("Token mismatch in Redis, updating to new token.");
				redisTemplate.opsForValue().set(redisKey, token, 1);
				return false;
			}

			if (jwtService.isTokenExpired(token)) {
				logger.warn("Token expired in Redis, updating to new token.");
				redisTemplate.opsForValue().set(redisKey, token, 1);
				return false;
			}

			return false;
		} catch (Exception e) {
			logger.error("Error while checking token in Redis, allowing access.", e);
			return true;
		}
	}
}
