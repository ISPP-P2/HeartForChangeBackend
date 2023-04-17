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

import com.ispp.heartforchange.dto.ComplementaryFormationDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.ComplementaryFormationServiceImpl;

@Controller
@RequestMapping("/complementaryFormations")
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
	 * Get complementary formation by id
	 * 
	 * @Param HttpServletRequest
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getComplementaryFormationById(HttpServletRequest request,
			@PathVariable("id") Long id) throws OperationNotAllowedException{
		
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			ComplementaryFormationDTO complementaryFormation = complementaryFormationServiceImpl.getComplementaryFormationById(id, jwt);
			return ResponseEntity.ok(complementaryFormation);
			
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
			
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/*
     * Get all complementary formation by volunteer
     * 
     * @Param HttpServletRequest
     * @Param Long id
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/volunteer/{id}")
    public ResponseEntity<?> getComplementaryFormationByVolunteer(HttpServletRequest request,
    		@PathVariable("id") Long id) throws OperationNotAllowedException{
        
    	String jwt = null;
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            jwt = headerAuth.substring(7, headerAuth.length());
        }
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
        }

        
        try {
        	
            List<ComplementaryFormationDTO> complementaryFormation = complementaryFormationServiceImpl.getComplementaryFormationByVolunteer(id, jwt);
            return ResponseEntity.ok(complementaryFormation);
            
        }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		
        }catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
        
    }
    
    
    
    /*
     * Get all complementary formation by beneficiary
     * 
     * @Param HttpServletRequest
     * @Param Long id
     * 
     * @Return ResponseEntity
     */
    @GetMapping("/get/beneficiary/{id}")
    public ResponseEntity<?> getComplementaryFormationByBeneficiary(HttpServletRequest request,
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
        	
            List<ComplementaryFormationDTO> complementaryFormation = complementaryFormationServiceImpl.getComplementaryFormationByBeneficiary(id, jwt);
            return ResponseEntity.ok(complementaryFormation);
            
        }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG or a volunteer to use this method.", HttpStatus.BAD_REQUEST);
		
        }catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}

    }
    
    
    /*
    * Save complementary formation
    * 
    * @Param ComplementaryFormationDTO complementaryFormationDTO
    * @Param Long id 
    * 
    * @Return ResponseEntity
    */
   @PostMapping("/save/{id}")
   public ResponseEntity<?> saveComplementaryFormation(HttpServletRequest request, 
		   @Valid @RequestBody ComplementaryFormationDTO complementaryFormationDTO, 
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
           ComplementaryFormationDTO complementaryFormationSaved = complementaryFormationServiceImpl.saveComplementaryFormation(complementaryFormationDTO, id, jwt);
           logger.info("Complementary Formation saved associated with id={}", id);
           return ResponseEntity.ok(complementaryFormationSaved);
      
       }catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		
       }catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
       }


   }
   
   
   /*
	 * Update complementary formation
	 * 
	 * @Param ComplementaryFormationDTO complementaryFormationDTO
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateComplementaryFormation(@Valid @RequestBody 
			ComplementaryFormationDTO complementaryFormationDTO, HttpServletRequest request, 
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
			ComplementaryFormationDTO complementaryFormationSaved = complementaryFormationServiceImpl.updateComplementaryFormation(jwt, complementaryFormationDTO, id);
			logger.info("Complementary formation saved with id={}", complementaryFormationSaved.getId());
			return ResponseEntity.ok(complementaryFormationSaved);
		
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		

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
			HttpServletRequest request) throws OperationNotAllowedException {
		
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			complementaryFormationServiceImpl.deleteComplementaryFormation(id, jwt);
			return ResponseEntity.ok("Complementary formation deleted");
			
		}catch(OperationNotAllowedException e) {
			return new ResponseEntity<String>("You must be an ONG to use this method.", HttpStatus.BAD_REQUEST);
		
		}catch(Exception e) {
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		

	}
}
