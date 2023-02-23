package com.ispp.hearthforchange.service;

import org.springframework.stereotype.Service;

import com.ispp.hearthforchange.dto.AccountDTO;

@Service
public interface AccountService {
	
	/*
	 * Create Account
	 * @param AccountDTO 
	 * @return The accountDto
	 */
	AccountDTO createAccount( AccountDTO  accountDto );
}
