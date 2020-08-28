package com.vladaspring.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
@Getter
public class AppProperties {
	
	@Value("${token.duration.millis}")
	private long tokenDurationMillis;
	
	@Value("${token.signature.key}")
	private String tokenKey;
	
}
