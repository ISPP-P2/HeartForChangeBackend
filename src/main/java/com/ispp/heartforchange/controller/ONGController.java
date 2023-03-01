package com.ispp.heartforchange.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.service.impl.OngServiceImpl;


@RestController
@RequestMapping("/ongs")
public class ONGController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private OngServiceImpl ongServiceImpl;

	/*
	 * Dependency injection
	 */
	public ONGController(OngServiceImpl ongServiceImpl) {
		super();
		this.ongServiceImpl = ongServiceImpl;
	}

	@GetMapping
	public ResponseEntity<?> getAllOngs() {
		List<OngDTO> ongs = ongServiceImpl.getAllOngs();
		return ResponseEntity.ok(ongs);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOngById(@PathVariable("id") Long id) {
		OngDTO ong = ongServiceImpl.getOngbyId(id);
		return ResponseEntity.ok(ong);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> saveOng(@Valid @RequestBody OngDTO ong) {
		OngDTO ongSaved = ongServiceImpl.saveOng(ong);
		return ResponseEntity.ok(ongSaved);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateOng(@Valid @RequestBody OngDTO ong) {
		OngDTO ongUpdated = ongServiceImpl.updateOng(ong);
		return ResponseEntity.ok(ongUpdated);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> deleteOng(@Valid @RequestBody OngDTO ong) {
		ongServiceImpl.deleteOng(ong);
		return ResponseEntity.ok().build();
	}
	
	
	
	
	


}
