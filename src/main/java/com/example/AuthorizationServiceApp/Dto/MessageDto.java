package com.example.AuthorizationServiceApp.Dto;

public class MessageDto {

	private String to;
	private String username;
	private String subject;
	private String body;

	public MessageDto(String to, String username, String subject, String body) {
		this.to = to;
		this.username = username;
		this.subject = subject;
		this.body = body;
	}

	public String getTo() {
		return to;
	}

	public String getUsername() {
		return username;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}
}
