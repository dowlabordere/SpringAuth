package com.vladaspring.api.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vladaspring.api.model.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(new Date());
		errorResponse.setLocation(request.getDescription(false));
		errorResponse.setStatusCode(status.value());
		errorResponse.setError(status.getReasonPhrase());
		errorResponse.setErrorDetails(List.of(ex.getMessage()));
		
		return new ResponseEntity<>(errorResponse, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(new Date());
		errorResponse.setLocation(request.getDescription(false));
		errorResponse.setStatusCode(status.value());
		errorResponse.setError(status.getReasonPhrase());
		List<String> errorDetails = new ArrayList<>();
		for (FieldError errorMessage : ex.getBindingResult().getFieldErrors()) {
			errorDetails.add(errorMessage.getDefaultMessage());
		}
		errorResponse.setErrorDetails(errorDetails);
		
		return new ResponseEntity<>(errorResponse, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(new Date());
		response.setLocation(request.getDescription(false));
		response.setError(status.getReasonPhrase());
		List<String> errorDetails = new ArrayList<>();
		for (FieldError errorMessage : ex.getBindingResult().getFieldErrors()) {
			errorDetails.add(errorMessage.getDefaultMessage());
		}
		response.setErrorDetails(errorDetails);
		response.setStatusCode(status.value());
		
		return new ResponseEntity<>(response, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(new Date());
		errorResponse.setLocation(request.getDescription(false));
		errorResponse.setStatusCode(status.value());
		errorResponse.setError(status.getReasonPhrase());
		errorResponse.setErrorDetails(List.of("Requests body is missing"));
		
		return new ResponseEntity<>(errorResponse, status);
	}
	
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<Object> handleExceptionInternal(BusinessException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(new Date());
		response.setStatusCode(ex.getHttpStatus().value());
		response.setErrorDetails(List.of(ex.getMessage()));
		response.setError(ex.getHttpStatus().getReasonPhrase());
		response.setLocation(ex.getLocation());
		
		return new ResponseEntity<>(response, ex.getHttpStatus());
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleExceptionInternal(ConstraintViolationException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(new Date());
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setErrorDetails(List.of(ex.getMessage()));
		response.setError(ex.getMessage());
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleExceptionInternal(DataIntegrityViolationException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(new Date());
		response.setStatusCode(HttpStatus.CONFLICT.value());
		response.setErrorDetails(List.of("Trenutno ne mogu nesto da obradim...vraticu se!!! dupli"));
		response.setError(ex.getMessage());
		////////////////////////////////////////////////////////
		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	
	

}
