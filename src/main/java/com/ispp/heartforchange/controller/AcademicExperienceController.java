package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.AcademicExperienceDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.AcademicExperienceService;
import com.ispp.heartforchange.service.impl.AcademicExperienceServiceImpl;

@RestController
@RequestMapping("/academicExps")
public class AcademicExperienceController {
	private static final Logger logger = LoggerFactory.getLogger(AcademicExperienceController.class);

	private AcademicExperienceService academicExpServImpl;
	private JwtUtils jwtUtils;

	/*
	 * Dependency injection
	 */
	public AcademicExperienceController(AcademicExperienceServiceImpl academicExpServImpl, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		super();
		this.academicExpServImpl = academicExpServImpl;
		this.jwtUtils = jwtUtils;
	}

	/*
	 * Get all academic experiences
	 * 
	 * @Params HttpServletRequest
	 * 
	 * @Return ResponseEntity<AcademicExperienceDTO>
	 */
	@GetMapping
	public ResponseEntity<?> getAllAcademicExperiences() {

		List<AcademicExperienceDTO> academicExps = academicExpServImpl.getAllAcademicExp();

		return ResponseEntity.ok(academicExps);
	}

	/*
	 * Get all grant by volunteer
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/volunteer/{username}")
	public ResponseEntity<?> getAcademicExperienceByVolunteerUsername(HttpServletRequest request,
			@PathVariable("username") String username) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		List<AcademicExperienceDTO> acadExp = academicExpServImpl.getAcademicExperienceByVolunteerUsername(username,
				jwt);
		return ResponseEntity.ok(acadExp);
	}

	/*
	 * Get all grant by beneficiary
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/beneficiary/{username}")
	public ResponseEntity<?> getAcademicExperienceByBeneficiaryUsername(HttpServletRequest request,
			@PathVariable("username") String username) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		List<AcademicExperienceDTO> acadExp = academicExpServImpl.getAcademicExperienceByBeneficiaryUsername(username,
				jwt);
		return ResponseEntity.ok(acadExp);
	}

	/*
	 * Get Academic Experience by volunteer
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Param id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getAcademicExperienceByID(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		AcademicExperienceDTO academicExperienceDTO = academicExpServImpl.getAcademicExpByID(id, jwt);
		return ResponseEntity.ok(academicExperienceDTO);
	}

	/*
	 * Save academic experience
	 * 
	 * @Param academicExperienceDTO
	 * 
	 * @Param username
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/save/{username}")
	public ResponseEntity<?> saveBeneficiary(HttpServletRequest request,
			@Valid @RequestBody AcademicExperienceDTO academicExperienceDTO,
			@PathVariable("username") String username) {

		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		AcademicExperienceDTO academicExperienceSaved = academicExpServImpl
				.saveAcademicExperience(academicExperienceDTO, username);
		logger.info("Experience Academic saved associated with {}", username);
		return ResponseEntity.ok(academicExperienceSaved);
	}

	/*
	 * Update Academic Experience
	 * 
	 * @Param AcademicExperienceDTO
	 * 
	 * @Param token
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update")
	public ResponseEntity<?> updateAcademicExperience(@Valid @RequestBody AcademicExperienceDTO academicExperienceDTO,
			HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		AcademicExperienceDTO academicExperienceSaved = academicExpServImpl
				.updateAcademicExperience(academicExperienceDTO, jwt);
		logger.info("Academic Experience saved with id={}", academicExperienceSaved.getId());
		return ResponseEntity.ok(academicExperienceSaved);
	}

	/*
	 * Delete academic experience
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteAcademicExperience(@PathVariable("id") Long id, HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		academicExpServImpl.deleteAcademicExperience(id, jwt);
		return ResponseEntity.ok("Grant deleted");
	}

}
