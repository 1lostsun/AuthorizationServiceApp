package com.example.AuthorizationServiceApp.Dto;

public class MessageDto {

	private String to;
	private String username;
	private String template;
	private String subject;
	private String body;

	public MessageDto(String to, String username, String template, String subject, String body) {
		this.to = to;
		this.username = username;
		this.template = template;
		this.subject = subject;
		this.body = body;
	}

	public String getTo() {
		return to;
	}

	public String getUsername() {
		return username;
	}

	public String getTemplate() {
		return template;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}
}
