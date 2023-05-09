package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.ispp.heartforchange.exceptions.AlreadyExists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.UpdatePasswordDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.VolunteerServiceImpl;


@RestController
@RequestMapping("/volunteers")
public class VolunteerController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private VolunteerServiceImpl volunteerServiceImpl;
	private JwtUtils jwtUtils;
	
	/*
	 * Dependency injection
	 */
	public VolunteerController(VolunteerServiceImpl volunteerServiceImpl, JwtUtils jwtUtils) {
		super();
		this.volunteerServiceImpl = volunteerServiceImpl;
		this.jwtUtils = jwtUtils;
	}
	

	/*
	 * Get all volunteers by ONG
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get")
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
	 * Get number of volunteers by ONG
	 * 
	 * @Param HttpServletRequest
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
	 * @Param HttpServletRequest
	 * @Param id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
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
	 * @Param HttpServletRequest
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

		try{
			VolunteerDTO volunteerSave = volunteerServiceImpl.saveVolunteer(volunteer, jwt);
			logger.info("Volunteer saved with username={}", volunteerSave.getUsername());
			return ResponseEntity.ok(volunteerSave);
		}catch(AlreadyExists e) {
			System.out.println(e);
			return ResponseEntity.status(436).body(e.getMessage());
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		

	}
	 
	/*
	 * Update volunteer
	 * 
	 * @Param HttpServletRequest
	 * @Param id
	 * @Param volunteerDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateVolunteer(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody VolunteerDTO volunteer) {
		
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

	    logger.info("Volunteer updated with id={}", id);
	    return ResponseEntity.ok().body(volunteerToUpdate);
	}
	
	/*
	 * Update volunteer
	 * 
	 * @Param HttpServletRequest
	 * @Param id
	 * @Param volunteerDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}/password")
	public ResponseEntity<?> updateVolunteerPassword(HttpServletRequest request, @PathVariable("id") Long id, @Valid @RequestBody UpdatePasswordDTO passwordDTO) {
		
		String jwt2 = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt2 = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt2 == null || !jwtUtils.validateJwtToken(jwt2)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}
				
	    VolunteerDTO volunteerToUpdate = volunteerServiceImpl.updateVolunteerPassword(id, passwordDTO, jwt2);
	    
	    logger.info("Volunteer password updated");
	    return ResponseEntity.ok().body(volunteerToUpdate);
	}
	
	
	/*
	 * Delete volunteer
	 * 
	 * @Param HttpServletRequest
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
