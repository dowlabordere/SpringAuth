package com.vladaspring.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vladaspring.api.hibernate.entities.User;
import com.vladaspring.api.model.request.NewAccReq;
import com.vladaspring.api.service.IdentityService;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

	@Autowired
	private IdentityService identityService;
	
    @GetMapping(path = "/login")
    public String login(){
        return "Loaded";
    }
    
    @PostMapping(path = "/account")
    public ResponseEntity<User> createAccount(@Valid @RequestBody NewAccReq request) {
    	
    	User user = identityService.createAccount(request.getUsername(), request.getPassword() ,request.getEmail());
    	
    	return ResponseEntity.ok(user);
    }

}
