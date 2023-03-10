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
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.WorkExperienceServiceImpl;

@Controller
@RequestMapping("/workExperience")
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
	 * Get all work experiences
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping
	public ResponseEntity<?> getAllWorkExperiences() {
		List<WorkExperienceDTO> workExperiences = workExperienceService.getAllWorkExperiences();
		return ResponseEntity.ok(workExperiences);
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
	public ResponseEntity<?> getWorkExperienceById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		WorkExperienceDTO workExperience = workExperienceService.getWorkExperienceById(id, jwt);
		return ResponseEntity.ok(workExperience);
	}
	
	
	/*
     * Get all work experiences by volunteer username
     * 
     * @Param HttpServletRequest
     * @Param String username
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/volunteer/{username}")
    public ResponseEntity<?> getWorkExperienceByVolunteerUsername(HttpServletRequest request,
            @PathVariable("username") String username) {
        String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        List<WorkExperienceDTO> workExperiences = workExperienceService.getWorkExperienceByVolunteerUsername(username, jwt);
        return ResponseEntity.ok(workExperiences);
    }
    
    
    /*
     * Get all work experiences by beneficiary username
     * 
     * @Param HttpServletRequest
     * @Param String username
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/beneficiary/{username}")
    public ResponseEntity<?> getWorkExperienceByBeneficiaryUsername(HttpServletRequest request,
            @PathVariable("username") String username) {
        String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        List<WorkExperienceDTO> workExperiences = workExperienceService.getWorkExperienceByBeneficiaryUsername(username, jwt);
        return ResponseEntity.ok(workExperiences);
    }
    
    
    /*
    * Save work experience
    * 
    * @Param WorkExperienceDTO workExperienceDTO
    * @Param String username 
    * 
    * @Return ResponseEntity
    */
   @PostMapping("/save/{username}")
   public ResponseEntity<?> saveWorkExperience(HttpServletRequest request, @Valid @RequestBody WorkExperienceDTO workExperienceDTO, 
           @PathVariable("username") String username) {

       String jwt = null;

       String headerAuth = request.getHeader("Authorization");

       if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
           jwt = headerAuth.substring(7, headerAuth.length());
       }
       if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
           return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
       }

       WorkExperienceDTO workExperienceSaved = workExperienceService.saveWorkExperience(workExperienceDTO, username);
       logger.info("Work Academic saved associated with {}", username);
       return ResponseEntity.ok(workExperienceSaved);
   }
   
   
   /*
	 * Update work experience
	 * 
	 * @Param WorkExperienceDTO workExperienceDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update")
	public ResponseEntity<?> updateWorkExperience(@Valid @RequestBody WorkExperienceDTO workExperienceDTO, HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		WorkExperienceDTO workExperienSaved = workExperienceService.updateWorkExperience(jwt, workExperienceDTO);
		logger.info("Work Experience saved with id={}", workExperienSaved.getId());
		return ResponseEntity.ok(workExperienSaved);
	}
	
	
	/*
	 * Delete work experience
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteWorkExperience(@PathVariable("id") Long id, HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		workExperienceService.deleteWorkExperience(id, jwt);
		return ResponseEntity.ok("Work Experience deleted");
	}
	
}
