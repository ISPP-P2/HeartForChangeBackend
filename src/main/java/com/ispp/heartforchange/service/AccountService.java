package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AccountDTO;
import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;

@Service
public interface AccountService {

	AccountDTO createAccount(AccountDTO accountDto);

	SigninResponseDTO authenticateUser(SigninRequestDTO accountDto);
	
	AccountDTO getAccountById(Long id);
	
	List<AccountDTO> getAllAccounts();
}
