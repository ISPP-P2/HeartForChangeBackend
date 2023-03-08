package com.ispp.heartforchange.service;


import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;

@Service
public interface AccountService {

	SigninResponseDTO authenticateUser(SigninRequestDTO accountDto);
}
