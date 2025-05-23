package com.example.AuthorizationServiceApp.Dto;

public class AuditMessageDto {

	private String eventType;
	private String username;
	private String ipAddress;
	private String timestamp;
	private String additionalInfo;
	private Long userId;

	public AuditMessageDto(String eventType, String username, String ipAddress, String timestamp, String additionalInfo, Long userId) {
		this.eventType = eventType;
		this.username = username;
		this.ipAddress = ipAddress;
		this.timestamp = timestamp;
		this.additionalInfo = additionalInfo;
		this.userId = userId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
