package com.ispp.heartforchange.service;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AccountDTO;
import com.ispp.heartforchange.dto.SigninDTO;

@Service
public interface AccountService {

	AccountDTO createAccount(AccountDTO accountDto);

	SigninDTO authenticateUser(AccountDTO accountDto);
}
