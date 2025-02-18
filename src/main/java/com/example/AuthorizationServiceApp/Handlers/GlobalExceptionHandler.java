package com.example.AuthorizationServiceApp.Handlers;

import com.example.AuthorizationServiceApp.Exceptions.IncorrectPasswordException;
import com.example.AuthorizationServiceApp.Exceptions.MessageSendingException;
import com.example.AuthorizationServiceApp.Exceptions.UserAlreadyExistsException;
import com.example.AuthorizationServiceApp.Exceptions.UserNotFoundException;
import com.example.AuthorizationServiceApp.Entities.ErrorResponseEntity;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
		ErrorResponseEntity errorResponse = new ErrorResponseEntity(new Date(), e.getMessage(), HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(IncorrectPasswordException.class)
	public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException e) {
		ErrorResponseEntity errorResponse = new ErrorResponseEntity(new Date(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
		ErrorResponseEntity errorResponse = new ErrorResponseEntity(new Date(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		ErrorResponseEntity errorResponse = new ErrorResponseEntity(new Date(), e.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
		ErrorResponseEntity errorResponse = new ErrorResponseEntity(new Date(), "Page is not found", HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(MessageSendingException.class)
	public ResponseEntity<?> handleMessageSendingException(MessageSendingException e) {
		ErrorResponseEntity errorResponse = new ErrorResponseEntity(new Date(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
		ErrorResponseEntity errorResponse = new ErrorResponseEntity(new Date(), e.getMessage(), HttpStatus.CONFLICT.value());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

}
