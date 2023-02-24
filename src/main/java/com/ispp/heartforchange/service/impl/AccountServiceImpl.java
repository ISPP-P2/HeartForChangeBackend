package com.ispp.heartforchange.service.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ispp.heartforchange.dto.AccountDTO;
import com.ispp.heartforchange.dto.SigninDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.repository.AccountRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private AccountRepository accountRepository;
	private PasswordEncoder encoder;
	private AuthenticationManager authenticationManager;
	private JwtUtils jwtUtils;
	
	/*
	 * Dependency injection 
	 */
	public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder encoder,
			AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
		super();
		this.accountRepository = accountRepository;
		this.encoder = encoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
	}
	
	/*
	 * Save an account.
	 * @Params accountDto
	 * @Return AccountDto
	 */
	@Override
	public AccountDTO createAccount(AccountDTO accountDto) {
		if (!Objects.isNull(accountRepository.findByUsername(accountDto.getUsername()))) {
			throw new UsernameNotFoundException("Account already exists!");
		}
		accountDto.setId(Long.valueOf(0));

		// Create new account
		accountDto.setPassword(encoder.encode(accountDto.getPassword()));
		logger.info("Saving account with username = {}", accountDto.getUsername());
		Account account = new Account(accountDto);
		return new AccountDTO(accountRepository.save(account));
	}
	
	/*
	 * Generate jwt with the authentication
	 * @Params accountDto
	 * @Return SigninDTO
	 */
	@Override
	public SigninDTO authenticateUser(AccountDTO accountDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(accountDto.getUsername(), accountDto.getPassword()));
		logger.info("Atuhenticating account with username = {}", accountDto.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		String refresh = jwtUtils.generateJwtRefreshToken(authentication);
		return new SigninDTO(jwt, refresh);
	}
}
