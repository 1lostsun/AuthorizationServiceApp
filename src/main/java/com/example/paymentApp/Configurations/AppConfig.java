package com.example.paymentApp.Configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

	private String secretKey;
	private long expirationTime;

	public long getExpirationTime() {
		return expirationTime;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}
}
