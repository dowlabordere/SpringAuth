package com.vladaspring.api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vladaspring.api.security.UserPrincipal;

@RestController
public class EventController {
	
	@GetMapping(path = "/events")
	public ResponseEntity<Object> getEvents() {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return ResponseEntity.ok(userPrincipal);
	}

}
