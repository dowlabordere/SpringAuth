package com.vladaspring.api.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vladaspring.api.exceptions.BusinessException;
import com.vladaspring.api.hibernate.entities.User;
import com.vladaspring.api.hibernate.repositories.UserRepository;
import com.vladaspring.api.security.UserPrincipal;

@Service
public class IdentityService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authManager;

	private static final Logger log = LoggerFactory.getLogger(IdentityService.class);

	@Transactional(rollbackOn = Exception.class)
	public User createAccount(String username, String password, String email) {

		log.debug("Creating new account with username : {}", username);

		User user = new User(0, username, email, passwordEncoder.encode(password), "standard", true);
		userRepository.save(user);

		log.debug("Finished creating new account with username : {}", username);

		return user;

	}
	
	public void authenticate(String username, String password) {
		
		log.debug("Starting authenticating");
		
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = generateToken(username);
		
		log.debug("Finished authenticating");
	}

	private String generateToken(String username) {
		Date creationTime = new Date();
		Date expirationTime = new Date(creationTime.getTime()+20000);
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(()->{
			log.error("Username {} not found in database", username);
			return new BusinessException("Username not found in database", HttpStatus.NO_CONTENT);
		});
		return new UserPrincipal(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.isActive());
	}
	
}
