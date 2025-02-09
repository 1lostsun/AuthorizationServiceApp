package com.example.paymentApp.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	private String email;
	private String password;
	private String username;

	public UserDto() {}

	public UserDto(String email, String password, String username) {
		this.email = email;
		this.password = password;
		this.username = username;
	}
}
