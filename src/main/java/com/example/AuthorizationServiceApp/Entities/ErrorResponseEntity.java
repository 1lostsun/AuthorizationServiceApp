package com.example.AuthorizationServiceApp.Entities;

import java.util.Date;

public class ErrorResponseEntity {

	private Date timestamp;
	private String message;
	private Integer errorCode;

	public ErrorResponseEntity(Date timestamp, String message, Integer errorCode) {
		this.timestamp = timestamp;
		this.message = message;
		this.errorCode = errorCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
