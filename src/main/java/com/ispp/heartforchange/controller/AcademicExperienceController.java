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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.AcademicExperienceDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.AcademicExperienceService;
import com.ispp.heartforchange.service.impl.AcademicExperienceServiceImpl;

@RestController
@RequestMapping("/academicExperiences")
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
	 * Get all academics experiences of a volunteer
	 * 
	 * @Param HttpServletRequest request
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/volunteer/{id}")
	public ResponseEntity<?> getAcademicExperienceByVolunteerUsername(HttpServletRequest request,
			@PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			List<AcademicExperienceDTO> acadExp = academicExpServImpl.getAcademicExperienceByVolunteer(id, jwt);
			return ResponseEntity.ok(acadExp);
        }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}

		
	}

	/*
	 * Get all academics experiences of a beneficiary
	 * 
	 * @Param HttpServletRequest request
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/beneficiary/{id}")
	public ResponseEntity<?> getAcademicExperienceByBeneficiaryID(HttpServletRequest request,
			@PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		try {
        	List<AcademicExperienceDTO> academicExperiences = academicExpServImpl.getAcademicExperienceByBeneficiary(id, jwt);
            return ResponseEntity.ok(academicExperiences);
        }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Get Academic Experience by id
	 * 
	 * @Param HttpServletRequest request
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getAcademicExperienceById(HttpServletRequest request, @PathVariable("id") Long id) throws OperationNotAllowedException{
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			AcademicExperienceDTO academicExperienceDTO = academicExpServImpl.getAcademicExpByID(id, jwt);
			return ResponseEntity.ok(academicExperienceDTO);
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}


	}

	/*
	 * Save academic experience
	 * 
	 * @Param AcademicExperienceDTO academicExperienceDTO
	 * @Param HttpServletRequest request
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/save/{id}")
	public ResponseEntity<?> saveAcademicExperience(HttpServletRequest request,
			@Valid @RequestBody AcademicExperienceDTO academicExperienceDTO,
			@PathVariable("id") Long id) throws OperationNotAllowedException {

		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
	    	AcademicExperienceDTO academicExperienceSaved = academicExpServImpl.saveAcademicExperience(academicExperienceDTO, id, jwt);
	         logger.info("Academic Experience saved associated with id={}", id);
	         return ResponseEntity.ok(academicExperienceSaved);
	    }catch(OperationNotAllowedException e) {
	    	return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Update Academic Experience
	 * 
	 * @Param AcademicExperienceDTO academicExperienceDTO
	 * @Param HttpServletRequest request
	 * @Param String token
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateAcademicExperience(@Valid @RequestBody AcademicExperienceDTO academicExperienceDTO,
			HttpServletRequest request, @PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		try {
			AcademicExperienceDTO academicExperienSaved = academicExpServImpl.updateAcademicExperience(academicExperienceDTO, jwt, id);
			logger.info("Academic Experience updated with id={}", academicExperienSaved.getId());
			return ResponseEntity.ok(academicExperienSaved);
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Delete academic experience
	 * 
	 * @Param Long id
	 * @Param HttpServletRequest request
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteAcademicExperience(@PathVariable("id") Long id, HttpServletRequest request) throws OperationNotAllowedException{
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		try {
			academicExpServImpl.deleteAcademicExperience(id, jwt);
			return ResponseEntity.ok("Academic Experience deleted");
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}

}
