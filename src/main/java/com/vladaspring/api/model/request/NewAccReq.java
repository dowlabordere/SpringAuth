package com.vladaspring.api.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NewAccReq {

	@NotNull(message = "Username is missing")
	@NotEmpty(message = "Username cannot be empty")
	private String username;
	@NotNull(message = "Password is missing")
	@NotEmpty(message = "Password cannot be empty")
	private String password;
	@NotNull(message = "Email is missing")
	@NotEmpty(message = "Email cannot be empty")
	private String email;
	
	
	
}
