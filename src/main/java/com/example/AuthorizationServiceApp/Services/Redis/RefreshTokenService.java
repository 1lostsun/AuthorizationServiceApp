package com.example.AuthorizationServiceApp.Services.Redis;

import com.example.AuthorizationServiceApp.Configurations.Redis.RedisConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

	private final RedisTemplate<String, String> redisTemplate;
	private final long refreshTokenExpirationTime;

	public RefreshTokenService(RedisTemplate<String, String> redisTemplate, RedisConfig redisConfig) {
		this.redisTemplate = redisTemplate;
		this.refreshTokenExpirationTime = redisConfig.getRedisTokenExpirationTime();
	}

	public void saveRefreshToken(String username, String refreshToken) {
		redisTemplate.opsForValue().set("jwt:refresh:" + username, refreshToken, refreshTokenExpirationTime, TimeUnit.SECONDS);
	}

	public String getRefreshToken(String username) {
		return redisTemplate.opsForValue().get("jwt:refresh:" + username);
	}

	public void deleteRefreshToken(String username) {
		redisTemplate.delete("jwt:refresh:" + username);
	}
}
