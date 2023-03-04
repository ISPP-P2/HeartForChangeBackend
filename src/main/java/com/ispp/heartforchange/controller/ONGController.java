package com.ispp.heartforchange.controller;

import java.util.List;

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

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.OngServiceImpl;


@RestController
@RequestMapping("/ongs")
public class ONGController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private OngServiceImpl ongServiceImpl;
	private JwtUtils jwtUtils;
	private AuthenticationManager authenticationManager;

	/*
	 * Dependency injection
	 */
	public ONGController(OngServiceImpl ongServiceImpl, JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		super();
		this.ongServiceImpl = ongServiceImpl;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
	}
	
	/*
	 * Get all ongs
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping
	public ResponseEntity<?> getAllOngs() {
		List<OngDTO> ongs = ongServiceImpl.getAllOngs();
		return ResponseEntity.ok(ongs);
	}
	
	/*
	 * Get ong by id
	 * 
	 * @Paramas id
	 * 
	 * @Return ResponseEntity
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getOngById(@PathVariable("id") Long id) {
		OngDTO ong = ongServiceImpl.getOngById(id);
		return ResponseEntity.ok(ong);
	}
	
	/*
	 * Signup ong
	 * 
	 * @Param OngDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> saveOng(@Valid @RequestBody OngDTO ong) {
		OngDTO ongSaved = ongServiceImpl.saveOng(ong);
		logger.info("ONG saved with username={}", ongSaved.getUsername());
		return ResponseEntity.ok(ongSaved);
	}
	
	/*
	 * Update ong
	 * 
	 * @Param id
	 * 
	 * @Param ongDTO
	 * 
	 * @Return ResponseEntity
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateOng(@PathVariable("id") Long id, @Valid @RequestBody OngDTO ong) {
	    OngDTO ongToUpdate = ongServiceImpl.updateOng(id, ong);
	    logger.info("Trying to authenticate with username={} and password={}", ongToUpdate.getUsername(), ongToUpdate.getPassword());
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(ongToUpdate.getUsername(), ong.getPassword()));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    String refresh = jwtUtils.generateJwtRefreshToken(authentication);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.set("Authorization", jwt);
	    responseHeaders.set("Refresh", refresh);
	    logger.info("ONG updated with id={}", id);
	    return ResponseEntity.ok().headers(responseHeaders).body(ongToUpdate);
	}
	
	/*
	 * Delete ong
	 * 
	 * @Params Long id
	 * 
	 * @Return ResponseEntity
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteOng(@PathVariable("id") Long id) {
		ongServiceImpl.deleteOng(id);
		logger.info("ONG with id={} deleted", id);
		return new ResponseEntity<String>("ONG deleted", HttpStatus.OK);
	}
}