package com.vladaspring.api.model.response;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private Date timestamp;
	private int statusCode;
	private String error;
	private List<String> errorDetails;
	private String location;

}
