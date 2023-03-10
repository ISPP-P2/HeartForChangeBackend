package com.ispp.heartforchange.controller;

import java.util.List;

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
	private AuthenticationManager authenticationManager;
	

	/*
	 * Dependency injection
	 */
	public BeneficiaryController(BeneficiaryServiceImpl beneficiaryServiceImpl, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		super();
		this.beneficiaryServiceImpl = beneficiaryServiceImpl;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
	}
	
	
	/*
	 * Get all beneficiaries
	 * 
	 * @Params HttpServletRequest
	 * 
	 * @Return ResponseEntity<BeneficiaryDTO>
	 */
	@GetMapping
	public ResponseEntity<?> getAllBeneficiaries() {
		List<BeneficiaryDTO> beneficiaries = beneficiaryServiceImpl.getAllBeneficiares();
		return ResponseEntity.ok(beneficiaries);
	}
	
	
	/*
	 * Get beneficiary by id
	 * 
	 * @Paramas id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getBeneficiaryById(@PathVariable("id") Long id) {
		BeneficiaryDTO beneficiary = beneficiaryServiceImpl.getBeneficiaryById(id);
		return ResponseEntity.ok(beneficiary);
	}
	
	
	/*
	 * Get beneficiaries by ong
	 * 
	 * @Paramas username
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("ong/{username}")
	public ResponseEntity<?> getBeneficiariesByOng(@PathVariable("username") String username) {
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
	public ResponseEntity<?> saveBeneficiary(HttpServletRequest request, @Valid @RequestBody BeneficiaryDTO beneficiary) {
		
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtils.getUserNameFromJwtToken(jwt);

		BeneficiaryDTO beneficiarySaved = beneficiaryServiceImpl.saveBeneficiary(beneficiary, username);
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
	public ResponseEntity<?> updateBeneficiary(@PathVariable("id") Long id, @Valid @RequestBody BeneficiaryDTO beneficiary) {
	    BeneficiaryDTO beneficiaryToUpdate = beneficiaryServiceImpl.updateBeneficiary(id, beneficiary);
	    logger.info("Trying to authenticate with username={} and password={}", beneficiaryToUpdate.getUsername(), beneficiaryToUpdate.getPassword());
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(beneficiaryToUpdate.getUsername(), beneficiary.getPassword()));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    String refresh = jwtUtils.generateJwtRefreshToken(authentication);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.set("Authorization", jwt);
	    responseHeaders.set("Refresh", refresh);
	    logger.info("Beneficiary updated with id={}", id);
	    return ResponseEntity.ok().headers(responseHeaders).body(beneficiaryToUpdate);
	}
	
	/*
	 * Delete beneficiary
	 * 
	 * @Params Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteBeneficiary(@PathVariable("id") Long id) {
		beneficiaryServiceImpl.deteleBeneficiary(id);
		logger.info("Beneficiary with id={} deleted", id);
		return new ResponseEntity<String>("Beneficiary deleted", HttpStatus.OK);
	}
}
