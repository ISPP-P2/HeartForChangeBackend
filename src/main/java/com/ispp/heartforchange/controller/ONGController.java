package com.ispp.heartforchange.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.OngServiceImpl;


@RestController
@RequestMapping("/ongs")
public class ONGController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private OngServiceImpl ongServiceImpl;
	private JwtUtils jwtUtils;
	private AuthenticationManager authenticationManager;

	/*
	 * Dependency injection
	 */
	public ONGController(OngServiceImpl ongServiceImpl, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		super();
		this.ongServiceImpl = ongServiceImpl;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
	}
	
	/*
	 * Get ong by id
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get")
	public ResponseEntity<?> getOng(HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		try {
			OngDTO ong = ongServiceImpl.getOng(jwt);
			logger.info("ONG got with username={}", ong.getUsername());
			return ResponseEntity.ok(ong);
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		} 
		
	}
	
	/*
	 * Signup ong
	 * 
	 * @Param OngDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> saveOng(HttpServletRequest request,@Valid @RequestBody OngDTO ong) throws OperationNotAllowedException {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		try {
			OngDTO ongSaved = ongServiceImpl.saveOng(ong, jwt);
			logger.info("ONG saved with username={}", ongSaved.getUsername());
			return ResponseEntity.ok(ongSaved);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>("This account already exists or any field is wrong: "+e.toString(), HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Update ong
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Param ongDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update")
	public ResponseEntity<?> updateOng(HttpServletRequest request, @Valid @RequestBody OngDTO ong) throws OperationNotAllowedException {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			OngDTO ongToUpdate = ongServiceImpl.updateOng(jwt, ong);
			
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			logger.info("ONG updated with username={}", username);
			
			//To refresh the token after updating
		    Authentication authentication = authenticationManager.authenticate(
		            new UsernamePasswordAuthenticationToken(ongToUpdate.getUsername(), ong.getPassword()));
		    SecurityContextHolder.getContext().setAuthentication(authentication);
		    String jwt2 = jwtUtils.generateJwtToken(authentication);
		    String refresh = jwtUtils.generateJwtRefreshToken(authentication);
		    HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.set("Authorization", jwt2);
		    responseHeaders.set("Refresh", refresh);
		    
		    return ResponseEntity.ok().headers(responseHeaders).body(ongToUpdate);
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>("That account already exists: "+e.toString(), HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/*
	 * Delete ong
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete")
	public ResponseEntity<?> deleteOng(HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		try {
			ongServiceImpl.deleteOng(jwt);
			
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			logger.info("ONG with username={} deleted", username);
			
			return new ResponseEntity<String>("ONG deleted", HttpStatus.OK);	
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
}