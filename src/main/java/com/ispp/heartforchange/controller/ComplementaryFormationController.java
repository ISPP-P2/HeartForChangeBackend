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

import com.ispp.heartforchange.dto.ComplementaryFormationDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.ComplementaryFormationServiceImpl;

@Controller
@RequestMapping("/complementaryFunction")
public class ComplementaryFormationController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private ComplementaryFormationServiceImpl complementaryFormationServiceImpl;
	private JwtUtils jwtUtils;
	

	/*
	 * Dependency injection
	 */
	public ComplementaryFormationController(ComplementaryFormationServiceImpl 
			complementaryFormationServiceImpl, JwtUtils jwtUtils) {
		
		super();
		this.complementaryFormationServiceImpl = complementaryFormationServiceImpl;
		this.jwtUtils = jwtUtils;
	}
	
	
	/*
	 * Get all complementary formation
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping
	public ResponseEntity<?> getAllComplementaryFormation() {
		List<ComplementaryFormationDTO> complementaryFormations = complementaryFormationServiceImpl.getAllComplementaryFormations();
		return ResponseEntity.ok(complementaryFormations);
	}
	
	
	/*
	 * Get complementary formation by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getComplementaryFormationById(HttpServletRequest request,
			@PathVariable("id") Long id) {
		
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		ComplementaryFormationDTO complementaryFormation = complementaryFormationServiceImpl.getComplementaryFormationById(id, jwt);
		return ResponseEntity.ok(complementaryFormation);
	}
	
	
	/*
     * Get all complementary formation by volunteer
     * 
     * @Param HttpServletRequest
     * @Param String username
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/volunteer/{username}")
    public ResponseEntity<?> getComplementaryFormationByVolunteer(HttpServletRequest request,
    		@PathVariable("username") String username) {
        
    	String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        List<ComplementaryFormationDTO> acadExp = complementaryFormationServiceImpl.getComplementaryFormationByVolunteer(username, jwt);
        return ResponseEntity.ok(acadExp);
    }
    
    
    
    /*
     * Get all complementary formationby beneficiary
     * 
     * @Param HttpServletRequest
     * @Param String username
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/beneficiary/{username}")
    public ResponseEntity<?> getComplementaryFormationByBeneficiary(HttpServletRequest request,
            @PathVariable("username") String username) {
    	
        String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        List<ComplementaryFormationDTO> acadExp = complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(username, jwt);
        return ResponseEntity.ok(acadExp);
    }
    
    
    /*
    * Save complementary formation
    * 
    * @Param ComplementaryFormationDTO complementaryFormationDTO
    * @Param String username 
    * 
    * @Return ResponseEntity
    */
   @PostMapping("/save/{username}")
   public ResponseEntity<?> saveComplementaryFormation(HttpServletRequest request, 
		   @Valid @RequestBody ComplementaryFormationDTO complementaryFormationDTO, 
		   @PathVariable("username") String username) {

       String jwt = null;

       String headerAuth = request.getHeader("Authorization");

       if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
           jwt = headerAuth.substring(7, headerAuth.length());
       }
       if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
           return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
       }

       ComplementaryFormationDTO complementaryFormationSaved = complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, username);
       logger.info("Complementary Formation saved associated with {}", username);
       return ResponseEntity.ok(complementaryFormationSaved);
   }
   
   
   /*
	 * Update complementary formation
	 * 
	 * @Param ComplementaryFormationDTO complementaryFormationDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update")
	public ResponseEntity<?> updateComplementaryFormation(@Valid @RequestBody 
			ComplementaryFormationDTO complementaryFormationDTO, HttpServletRequest request) {
		
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		ComplementaryFormationDTO complementaryFormationSaved = complementaryFormationServiceImpl.updateComplementaryFormation(jwt, complementaryFormationDTO);
		logger.info("Complementary formation saved with id={}", complementaryFormationSaved.getId());
		return ResponseEntity.ok(complementaryFormationSaved);
	}
	
	
	/*
	 * Delete complementary formation
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteComplementaryFormation(@PathVariable("id") Long id, 
			HttpServletRequest request) {
		
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		complementaryFormationServiceImpl.deleteComplementaryFormation(id, jwt);
		return ResponseEntity.ok("Complementary formation deleted");
	}
}
