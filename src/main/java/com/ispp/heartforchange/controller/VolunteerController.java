package com.ispp.heartforchange.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.VolunteerServiceImpl;

import org.springframework.util.StringUtils;


@RestController
@RequestMapping("/volunteers")
public class VolunteerController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private VolunteerServiceImpl volunteerServiceImpl;
	private JwtUtils jwtUtils;
	private AuthenticationManager authenticationManager;
	
	/*
	 * Dependency injection
	 */
	public VolunteerController(VolunteerServiceImpl volunteerServiceImpl, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		super();
		this.volunteerServiceImpl = volunteerServiceImpl;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
	}
	

	/*
	 * Get all volunteer by ONG
	 * 
	 * @Params username
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/ong")
	public ResponseEntity<?> getVolunteersByOng(HttpServletRequest request){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		List<VolunteerDTO> volunteers = volunteerServiceImpl.getVolunteersByOng(username);
		return ResponseEntity.ok(volunteers);
	}
	
	/*
	 * Get all volunteer by ONG
	 * 
	 * @Params username
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/total")
	public ResponseEntity<?> getNumberOfVolunteersByOng(HttpServletRequest request){
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		Integer volunteers = volunteerServiceImpl.getNumberOfVolunteersByOng(username);
		return ResponseEntity.ok(volunteers);
	}
	
	/*
	 * Get volunteer by id
	 * 
	 * @Param id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getVolunteerById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		VolunteerDTO volunteer = volunteerServiceImpl.getVolunteerById(id, username);
		return ResponseEntity.ok(volunteer);
	}
	
	/*
	 * Signup volunteer
	 * 
	 * @Param volunteerDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> saveVolunteer(HttpServletRequest request,@RequestBody VolunteerDTO volunteer){
		
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
		String[] randomUUID = UUID.randomUUID().toString().split("-");

		String usernameGenerated = volunteer.getName().toLowerCase().substring(0, 3) + "-" + randomUUID[0].substring(0, 3);
		volunteer.setPassword(usernameGenerated);
		volunteer.setUsername(usernameGenerated);

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		
		VolunteerDTO volunteerSave = volunteerServiceImpl.saveVolunteer(volunteer, username);
		logger.info("Volunteer saved with username={}", volunteerSave.getUsername());
		return ResponseEntity.ok(volunteerSave);
	}
	 
	/*
	 * Update volunteer
	 * 
	 * @Param id
	 * 
	 * @Param volunteerDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateVolunteer(HttpServletRequest request, @PathVariable("id") Long id, @Valid @RequestBody VolunteerDTO volunteer) {
		
		String jwt2 = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt2 = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt2 == null || !jwtUtils.validateJwtToken(jwt2)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt2);
		
	    VolunteerDTO volunteerToUpdate = volunteerServiceImpl.updateVolunteer(id, volunteer, username);
	    logger.info("Trying to authenticate with username={} and password={}", volunteerToUpdate.getUsername(), volunteerToUpdate.getPassword());
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(volunteerToUpdate.getUsername(), volunteer.getPassword()));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    String refresh = jwtUtils.generateJwtRefreshToken(authentication);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.set("Authorization", jwt);
	    responseHeaders.set("Refresh", refresh);
	    logger.info("Volunteer updated with id={}", id);
	    return ResponseEntity.ok().headers(responseHeaders).body(volunteerToUpdate);
	}
	
	
	/*
	 * Delete volunteer
	 * 
	 * @Params Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteVolunteer(HttpServletRequest request, @PathVariable("id") Long id) {
		
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		
		volunteerServiceImpl.deleteVolunteer(id, username);
		logger.info("Volunteer with id={} deleted", id);
		return new ResponseEntity<String>("Volunteer deleted", HttpStatus.OK);
	}
}
