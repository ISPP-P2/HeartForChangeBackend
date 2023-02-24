package com.ispp.heartforchange.controller;



import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.AccountDTO;
import com.ispp.heartforchange.dto.SigninDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.security.services.AccountDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	Logger log = LoggerFactory.getLogger( AccountController.class );
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	AccountDetailsServiceImpl accountDetailsServiceImpl;
	
		
	  @PostMapping("/signup")
	  public ResponseEntity<?> registerUser(AccountDTO signUpRequest) {
	    if ( ! Objects.isNull(accountRepository.findByUsername(signUpRequest.getUsername()))) {
	    	throw new UsernameNotFoundException("Account already exists!");
	    }
	    signUpRequest.setId(Long.valueOf(0));
	    // Create new account
	    signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));
	    Account account = new Account(signUpRequest);


	    return ResponseEntity.ok(new AccountDTO(accountRepository.save(account)));
	  }
	  
	  @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody AccountDTO loginRequest) {

	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	  
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    String refresh = jwtUtils.generateJwtRefreshToken(authentication);
	 
	    return ResponseEntity.ok(new SigninDTO(jwt, 
	                         refresh));
	  }
	  
	  @GetMapping("/refresh")
	  public ResponseEntity<?> getAccounts(HttpServletRequest request) {
		  
		  try {
		      String jwt = null; 
		      
		      String headerAuth = request.getHeader("Authorization");

			  if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			      jwt = headerAuth.substring(7, headerAuth.length());
			  }
		      if( jwt == null || ! jwtUtils.validateJwtToken(jwt)) {
		    	  return new ResponseEntity<String>("JWT no valid to refresh",HttpStatus.BAD_REQUEST);
		      } 
		      
		        String username = jwtUtils.getUserNameFromJwtToken(jwt);
		        

		        UserDetails userDetails = accountDetailsServiceImpl.loadUserByUsername(username);
		        UsernamePasswordAuthenticationToken authentication =
		            new UsernamePasswordAuthenticationToken(
		                userDetails,
		                null,
		                userDetails.getAuthorities());
		        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		        SecurityContextHolder.getContext().setAuthentication(authentication);
		      
		      String jwt2 = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().getAuthentication());
			  String refresh = jwtUtils.generateJwtRefreshToken(SecurityContextHolder.getContext().getAuthentication());
			  return ResponseEntity.ok(new SigninDTO(jwt2, 
		                refresh));
		    } catch (Exception e) {
		      logger.error("Cannot refresh: {}", e);
		    }  
		  return new ResponseEntity<String>("JWT no valid to refresh",HttpStatus.BAD_REQUEST);
	  }
	  
	  
}
