package com.emblproject.moses.Exception;

import java.util.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND, new Date(),
				ex.getMessage(), details);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleException(DataIntegrityViolationException ex){
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.CONFLICT,
				new Date(),
				"Data Integrity Violation",details);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<Object> handleException(UnAuthorizedException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, new Date(),
				ex.getMessage(), details);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<Object> handleException(DuplicateKeyException ex){
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.CONFLICT,
				new Date(),
				"Similar record already exist",details);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ExceptionResponse exceptionResponse = new ExceptionResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				new Date(), "Something went wrong while trying to process your request",
				details);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@ExceptionHandler(PersonException.class)
	public ResponseEntity<Object> handlePersonException(PersonException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		final ExceptionResponse exceptionResponse = new ExceptionResponse(
				HttpStatus.BAD_REQUEST, new Date(), ex.getMessage(),
				details);
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers, HttpStatus status, WebRequest request) {

		final List<String> errors = new ArrayList<>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, new Date(), null,
				errors);
		return handleExceptionInternal(ex, exceptionResponse, headers, exceptionResponse.getStatus(), request);

	}

}