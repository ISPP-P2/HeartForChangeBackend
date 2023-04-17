package com.ispp.heartforchange.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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

import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.BeneficiaryServiceImpl;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController {
	private static final Logger logger = LoggerFactory.getLogger(BeneficiaryController.class);

	private BeneficiaryServiceImpl beneficiaryServiceImpl;
	private JwtUtils jwtUtils;
	

	/*
	 * Dependency injection
	 */
	public BeneficiaryController(BeneficiaryServiceImpl beneficiaryServiceImpl, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		super();
		this.beneficiaryServiceImpl = beneficiaryServiceImpl;
		this.jwtUtils = jwtUtils;
	}
	
	
	
	
	/*
	 * Get beneficiary by id
	 * 
	 * @Paramas id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getBeneficiaryById(@PathVariable("id") Long id, HttpServletRequest request) {
		
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");
		String username = null;
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			username = jwtUtils.getUserNameFromJwtToken(jwt);
		}catch(Exception e){
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		
		BeneficiaryDTO beneficiary = beneficiaryServiceImpl.getBeneficiaryById(id,username);
		return ResponseEntity.ok(beneficiary);
	}
	
	
	/*
	 * Get beneficiaries by ong
	 * 
	 * @Paramas username
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/total")
	public ResponseEntity<?> getNumberBneneficiariesByOng(HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");
		String username = null;
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			username = jwtUtils.getUserNameFromJwtToken(jwt);
		}catch(Exception e){
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		Integer totalBeneficiaries = beneficiaryServiceImpl.getNumberBeneficiaresByOng(username);
		return ResponseEntity.ok(totalBeneficiaries);
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getBeneficiariesByOng(HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");
		String username = null;
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}
		
		try {
			username = jwtUtils.getUserNameFromJwtToken(jwt);
		}catch(Exception e){
			return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
		}
		List<BeneficiaryDTO> beneficiaries = beneficiaryServiceImpl.getAllBeneficiaresByOng(username);
		return ResponseEntity.ok(beneficiaries);
	}
	
	
	/*
	 * Signup beneficiary
	 * 
	 * @Param beneficiaryDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> saveBeneficiary(HttpServletRequest request,  @RequestBody BeneficiaryDTO beneficiary) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		} 
		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		String[] randomUUID = UUID.randomUUID().toString().split("-");

		String usernameGenerated = beneficiary.getName().toLowerCase().substring(0, 3) + "-" + randomUUID[0].substring(0, 3);
		beneficiary.setPassword(usernameGenerated);
		beneficiary.setUsername(usernameGenerated);

		BeneficiaryDTO beneficiarySaved = beneficiaryServiceImpl.saveBeneficiary(beneficiary, username);
		System.out.println(beneficiarySaved);
		logger.info("Beneficiary saved with username={}", beneficiarySaved.getUsername());
		return ResponseEntity.ok(beneficiarySaved);
	}
	
	
	/*
	 * Update beneficiary
	 * 
	 * @Param id
	 * 
	 * @Param beneficiaryDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateBeneficiary(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody BeneficiaryDTO beneficiary) {
		String jwt2 = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt2 = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt2 == null || !jwtUtils.validateJwtToken(jwt2)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt2);

	    BeneficiaryDTO beneficiaryToUpdate = beneficiaryServiceImpl.updateBeneficiary(id, beneficiary, username);
	    logger.info("Beneficiary with id={} updated", id);
	    return ResponseEntity.ok().body(beneficiaryToUpdate);
	}
	
	/*
	 * Delete beneficiary
	 * 
	 * @Params Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteBeneficiary(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt);
		
		beneficiaryServiceImpl.deteleBeneficiary(id, username);
		logger.info("Beneficiary with id={} deleted", id);
		return new ResponseEntity<String>("Beneficiary deleted", HttpStatus.OK);
	}
}
