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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ispp.heartforchange.dto.WorkExperienceDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.WorkExperienceServiceImpl;

@Controller
@RequestMapping("/workExperiences")
public class WorkExperienceController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	private WorkExperienceServiceImpl workExperienceService;
	private JwtUtils jwtUtils;
	
	/*
	 * Dependency injection
	 */
	public WorkExperienceController(WorkExperienceServiceImpl workExperienceService, JwtUtils jwtUtils) {
		super();
		this.workExperienceService = workExperienceService;
		this.jwtUtils = jwtUtils;
	}
	
	
	
	/*
	 * Get work experience by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getWorkExperienceById(HttpServletRequest request, @PathVariable("id") Long id) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			WorkExperienceDTO workExperience = workExperienceService.getWorkExperienceById(id, jwt);
			return ResponseEntity.ok(workExperience);
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/*
     * Get all work experiences by volunteer id
     * 
     * @Param HttpServletRequest
     * @Param Long id
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/volunteer/{id}")
    public ResponseEntity<?> getWorkExperienceByVolunteerUsername(HttpServletRequest request,
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
        	List<WorkExperienceDTO> workExperiences = workExperienceService.getWorkExperienceByVolunteer(id, jwt);
            return ResponseEntity.ok(workExperiences);
        }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
        
    }
    
    
    /*
     * Get all work experiences by beneficiary id
     * 
     * @Param HttpServletRequest
     * @Param Long id
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/beneficiary/{id}")
    public ResponseEntity<?> getWorkExperienceByBeneficiaryUsername(HttpServletRequest request,
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
        	List<WorkExperienceDTO> workExperiences = workExperienceService.getWorkExperienceByBeneficiary(id, jwt);
            return ResponseEntity.ok(workExperiences);
        }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
    }
    
    
    /*
    * Save work experience
    * 
    * @Param WorkExperienceDTO workExperienceDTO
    * @Param Long id
    * 
    * @Return ResponseEntity
    */
   @PostMapping("/save/{id}")
   public ResponseEntity<?> saveWorkExperience(HttpServletRequest request, @Valid @RequestBody WorkExperienceDTO workExperienceDTO, 
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
    	   WorkExperienceDTO workExperienceSaved = workExperienceService.saveWorkExperience(workExperienceDTO, id, jwt);
           logger.info("Work Experience saved associated with id={}", id);
           return ResponseEntity.ok(workExperienceSaved);
       }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
   }
   
   
   /*
	 * Update work experience
	 * 
	 * @Param WorkExperienceDTO workExperienceDTO
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateWorkExperience(@Valid @RequestBody WorkExperienceDTO workExperienceDTO, HttpServletRequest request,
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
			WorkExperienceDTO workExperienSaved = workExperienceService.updateWorkExperience(jwt, workExperienceDTO, id);
			logger.info("Work Experience updated with id={}", workExperienSaved.getId());
			return ResponseEntity.ok(workExperienSaved);
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/*
	 * Delete work experience
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteWorkExperience(@PathVariable("id") Long id, HttpServletRequest request) throws OperationNotAllowedException {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			workExperienceService.deleteWorkExperience(id, jwt);
			return ResponseEntity.ok("Work Experience deleted");
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
