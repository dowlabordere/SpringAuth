package com.vladaspring.api.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladaspring.api.hibernate.entities.User;
import com.vladaspring.api.hibernate.repositories.UserRepository;



@Service
public class IdentityService {
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger log = LoggerFactory.getLogger(IdentityService.class);
	
	@Transactional(rollbackOn = Exception.class)
	public User createAccount(String username, String password, String email) {
		
		
		log.debug("Creating new account with username : {}", username);
		
		User user = new User(0, username, email, password, "standard", true);
		userRepository.save(user);
		
		
		log.debug("Finished creating new account with username : {}", username);
		
		return user;
		
	}

}
