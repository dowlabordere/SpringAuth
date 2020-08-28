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

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class IdentityService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private AppProperties appProperties;

	private static final Logger log = LoggerFactory.getLogger(IdentityService.class);

	@Transactional(rollbackOn = Exception.class)
	public User createAccount(String username, String password, String email) {

		log.debug("Creating new account with username : {}", username);

		User user = new User(0, username, email, passwordEncoder.encode(password), "standard", true);
		userRepository.save(user);

		log.debug("Finished creating new account with username : {}", username);

		return user;

	}
	
	public String authenticate(String username, String password) {
		
		log.debug("Starting authenticating");
		
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = generateToken(username);
		
		log.debug("Finished authenticating");
		
		return token;
	}

	private String generateToken(String username) {
		Date creationTime = new Date();
		Date expirationTime = new Date(creationTime.getTime()+appProperties.getTokenDurationMillis());
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(creationTime)
				.setExpiration(expirationTime)
				.signWith(SignatureAlgorithm.HS512, appProperties.getTokenKey())
				.compact();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.debug("Finding user in database");
		
		User user = userRepository.findByUsername(username).orElseThrow(()->{
			log.error("Username {} not found in database", username);
			return new BusinessException("Username not found in database", HttpStatus.NO_CONTENT);
		});
		
		log.debug("Found user in database");
		
		return new UserPrincipal(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.isActive());
	}

	public boolean isTokenValid(String accessToken) {
		try {
			Jwts.parser().setSigningKey(appProperties.getTokenKey()).parseClaimsJws(accessToken);
			return true;
		} catch (SignatureException e) {
			log.error("***** Invalid JWT signature");
		} catch (MalformedJwtException e) {
			log.error("***** Invalid JWT token");
		} catch (ExpiredJwtException e) {
			log.error("***** Expired JWT token");
		} catch (UnsupportedJwtException e) {
			log.error("***** Unsupported JWT token");
		} catch (IllegalArgumentException e) {
			log.error("***** JWT claims string is empty");
		}
		return false;
	}

	public String getUserFromToken(String accessToken) {
		return Jwts.parser().setSigningKey(appProperties.getTokenKey()).parseClaimsJws(accessToken).getBody().getSubject();
	}

	public String deleteAccount() {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userRepository.findById(userPrincipal.getId()).get();
		userRepository.delete(user);
		return user.getUsername();
	}
	
}
