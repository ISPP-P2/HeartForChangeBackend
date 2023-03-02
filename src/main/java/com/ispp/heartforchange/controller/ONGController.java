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
		OngDTO ong = ongServiceImpl.getOngById(id);
		return ResponseEntity.ok(ong);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> saveOng(@Valid @RequestBody OngDTO ong) {
		OngDTO ongSaved = ongServiceImpl.saveOng(ong);
		return ResponseEntity.ok(ongSaved);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateOng(@PathVariable("id") Long id, @Valid @RequestBody OngDTO ong) {
		OngDTO ongToUpdate = ongServiceImpl.getOngById(id);
		ongToUpdate = ongServiceImpl.updateOng(id, ong);
		return ResponseEntity.ok(ongToUpdate);
	}
	
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteOng(@PathVariable("id") Long id) {
		ongServiceImpl.deleteOng(id);
		return ResponseEntity.ok().build();
	}
	
	
	
	
	


}
