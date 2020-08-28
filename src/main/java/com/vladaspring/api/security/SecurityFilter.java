package com.vladaspring.api.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vladaspring.api.hibernate.entities.User;
import com.vladaspring.api.service.IdentityService;

@Component
public class SecurityFilter extends OncePerRequestFilter{

	private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);
	
	@Autowired
	private IdentityService identityService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		String accessToken = request.getHeader("Authorization");
		if(accessToken!=null && !accessToken.isBlank()) {
			log.error("Access token is present in the request header params > {}", accessToken);
			boolean valid = identityService.isTokenValid(accessToken);
			if(valid) {
				String username = identityService.getUserFromToken(accessToken);
				UserDetails userDetails = identityService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}else {
			log.error("No access token is present in the request header params");
		}
		filterChain.doFilter(request, response);
		
	}
	
}
