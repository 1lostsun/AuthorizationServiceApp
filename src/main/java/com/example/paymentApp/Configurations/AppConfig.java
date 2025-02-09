package com.example.paymentApp.Configurations;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

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
