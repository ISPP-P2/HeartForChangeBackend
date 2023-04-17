package com.ispp.heartforchange.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.security.service.AccountDetailsServiceImpl;
import com.ispp.heartforchange.service.impl.AccountServiceImpl;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private JwtUtils jwtUtils;
	private AccountDetailsServiceImpl accountDetailsServiceImpl;
	private AccountServiceImpl accountServiceImpl;
	private AccountRepository accountRepository;

	/*
	 * Dependency injection
	 */
	public AccountController(JwtUtils jwtUtils, AccountDetailsServiceImpl accountDetailsServiceImpl,
			AccountServiceImpl accountServiceImpl, AccountRepository accountRepository) {
		super();
		this.jwtUtils = jwtUtils;
		this.accountDetailsServiceImpl = accountDetailsServiceImpl;
		this.accountServiceImpl = accountServiceImpl;
		this.accountRepository = accountRepository;
	}
	
	

	/*
	 * Sign in account
	 * 
	 * @Params AccountDTO
	 * 
	 * @Return ResponseEntity<SigninDTO>
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequestDTO loginRequest) {
		try{
			Account account = accountRepository.findByUsername(loginRequest.getUsername());
			if(!account.getRolAccount().equals(RolAccount.BENEFICIARY)) {
				SigninResponseDTO signinDto = accountServiceImpl.authenticateUser(loginRequest);
				logger.info("User loged with username {}:", loginRequest.getUsername());
				return ResponseEntity.ok(signinDto);
			}
		}catch(Exception e){
			logger.error("Cannot find the account: {}", e.getMessage());

		}
		
		
		
		return new ResponseEntity<String>("You don´t have permissions or account doesn´t exists", HttpStatus.BAD_REQUEST);
	}

	/*
	 * Refresh jwt of an account
	 * 
	 * @Params HttpServletRequest
	 * 
	 * @Return ResponseEntity<SigninDTO>
	 */
	@GetMapping("/refresh")
	public ResponseEntity<?> getAccounts(HttpServletRequest request) {

		try {
			String jwt = null;

			String headerAuth = request.getHeader("Authorization");

			if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
				jwt = headerAuth.substring(7, headerAuth.length());
			}
			if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
				return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
			}

			String username = jwtUtils.getUserNameFromJwtToken(jwt);

			UserDetails userDetails = accountDetailsServiceImpl.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			String jwt2 = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());
			String refresh = jwtUtils.generateJwtRefreshToken(SecurityContextHolder.getContext().getAuthentication());
			
			logger.info("Refresh token succesfully in user {}", username);
			return ResponseEntity.ok(new SigninResponseDTO(jwt2, refresh));
		} catch (Exception e) {
			logger.error("Cannot refresh: {}", e);
		}
		return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
	}
}
