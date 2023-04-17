package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ispp.heartforchange.dto.GrantDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.GrantServiceImpl;

@Controller
@RequestMapping("/grants")
public class GrantController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	private GrantServiceImpl grantService;
	private JwtUtils jwtUtils;
	
	/*
	 * Dependency injection
	 */
	public GrantController(GrantServiceImpl grantService, JwtUtils jwtUtils) {
		super();
		this.grantService = grantService;
		this.jwtUtils = jwtUtils;
	}
	
	/*
	 * Save grant
	 * 
	 * @Param GrantDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/save")
	public ResponseEntity<?> saveGrant(@Valid @RequestBody GrantDTO grant, HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			GrantDTO grantSaved = grantService.saveGrant( grant, jwt );
			logger.info("Grant saved with id={}", grantSaved.getId());
			return ResponseEntity.ok(grantSaved);
		}catch(OperationNotAllowedException e) {
  			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
  		}catch(Exception e) {
  			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
  		}

		
	}
	
	/*
	 * Get all grants by ong
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get")
	public ResponseEntity<?> getGrantsByOng(HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			List<GrantDTO> grants = grantService.getGrantsByOng(jwt);
			return ResponseEntity.ok(grants);
		}
		catch(OperationNotAllowedException e) {
  			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
  		}catch(Exception e) {
  			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
  		}
	}
	
	/*
	 * Get sum of the different amounts of the accepted grants by ong
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/amount")
	public ResponseEntity<?> getTotalAmountAcceptedGrantsByOng(HttpServletRequest request) throws OperationNotAllowedException {

		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		try {
			Double amount = grantService.getTotalAmountAcceptedGrantsByOng(jwt);
			return ResponseEntity.ok(amount); 
		}catch(OperationNotAllowedException e) {
  			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
  		}catch(Exception e) {
  			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
  		}
	}
	
	/*
	 * Get grant by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getGrantById(HttpServletRequest request, @PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			GrantDTO grant = grantService.getGrantById(id, jwt);
			return ResponseEntity.ok(grant);
		}catch(OperationNotAllowedException e) {
  			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
  		}catch(Exception e) {
  			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
  		}
	}
	
	/*
	 * Update grant
	 * 
	 * @Param GrantDTO
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateGrant(@Valid @RequestBody GrantDTO grant, HttpServletRequest request, @PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			GrantDTO grantSaved = grantService.updateGrant(jwt, grant, id);
			logger.info("Grant saved with id={}", grantSaved.getId());
			return ResponseEntity.ok(grantSaved);
		}catch(OperationNotAllowedException e) {
  			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
  		}catch(Exception e) {
  			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
  		}
	}
	
	/*
	 * Delete grant
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteGrant(@PathVariable("id") Long id, HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			grantService.deleteGrant(id, jwt);
			return ResponseEntity.ok("Grant deleted");
		}catch(OperationNotAllowedException e) {
  			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
  		}catch(Exception e) {
  			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
  		}
	}
	
}
