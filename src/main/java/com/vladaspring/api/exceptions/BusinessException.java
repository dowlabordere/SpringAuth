package com.vladaspring.api.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

// baca exceptione u runtimeu
@Getter
@Setter
public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 1837753460944325855L;
	
	private HttpStatus httpStatus;
	private String location;
	
	public BusinessException(String message, HttpStatus httpStatus){
		super(message);
		this.httpStatus = httpStatus;
	}
	
	public BusinessException(String message, HttpStatus httpStatus, String location){
		super(message);
		this.httpStatus = httpStatus;
		this.location = location;
	}
	
}
