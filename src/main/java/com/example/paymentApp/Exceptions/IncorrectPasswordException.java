package com.example.paymentApp.Exceptions;

public class IncorrectPasswordException extends RuntimeException {
	public IncorrectPasswordException(String message) {
		super(message);
	}

	public IncorrectPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectPasswordException(Throwable cause) {
		super(cause);
	}
}
