package com.ispp.heartforchange.service.impl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private AuthenticationManager authenticationManager;
	private JwtUtils jwtUtils;
	/*
	 * Dependency injection 
	 */
	public AccountServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
	}
	
	
	/*
	 * Generate jwt with the authentication
	 * @Params accountDto
	 * @Return SigninDTO
	 */
	@Override
	public SigninResponseDTO authenticateUser(SigninRequestDTO accountDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(accountDto.getUsername(), accountDto.getPassword()));
		logger.info("Authenticating account with username = {}", accountDto.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		String refresh = jwtUtils.generateJwtRefreshToken(authentication);
		return new SigninResponseDTO(jwt, refresh);
	}	
}
