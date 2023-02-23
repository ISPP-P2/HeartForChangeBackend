package com.ispp.heartforchange.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.hearthforchange.dto.AccountDTO;
import com.ispp.hearthforchange.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
	
	Logger log = LoggerFactory.getLogger( AccountController.class );
	
	private final AccountService accountService;
	
	
	@PostMapping("/register")
	public ResponseEntity<AccountDTO> saveAccount(@RequestBody AccountDTO accountDto ) {
		log.info("Creating account with username {}", accountDto.getUsername());
		AccountDTO newAccountDto = this.accountService.createAccount(accountDto);
		log.info("Created account with username {}", accountDto.getUsername());
		return new ResponseEntity<AccountDTO>(newAccountDto, HttpStatus.CREATED);
	}
}
