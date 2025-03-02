package com.example.AuthorizationServiceApp.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

	@Value("${app.secret-key}")
	private String secretKey;
	@Value("${app.access.token.expiration-time}")
	private int accessTokenExpirationTime;
	@Value("${app.refresh.token.expiration-time}")
	private int refreshTokenExpirationTime;

	public String getSecretKey() {
		return secretKey;
	}

	public int getAccessTokenExpirationTime() {
		return accessTokenExpirationTime;
	}

	public int getRefreshTokenExpirationTime() {
		return refreshTokenExpirationTime;
	}
}
