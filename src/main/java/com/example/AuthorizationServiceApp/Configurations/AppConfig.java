package com.example.AuthorizationServiceApp.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

	@Value("${app.secret-key}")
	private String secretKey;
	@Value("${app.expiration-time}")
	private int expirationTime;

	public int getExpirationTime() {
		return expirationTime;
	}

	public String getSecretKey() {
		return secretKey;
	}
}
