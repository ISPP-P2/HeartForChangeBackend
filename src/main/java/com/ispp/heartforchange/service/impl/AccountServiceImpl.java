package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import com.ispp.heartforchange.dto.OngDTO;
import com.ispp.heartforchange.dto.SigninRequestDTO;
import com.ispp.heartforchange.dto.SigninResponseDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
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
		accountDto.setRolAccount(RolAccount.ONG);

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
	public SigninResponseDTO authenticateUser(SigninRequestDTO accountDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(accountDto.getUsername(), accountDto.getPassword()));
		logger.info("Atuhenticating account with username = {}", accountDto.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		String refresh = jwtUtils.generateJwtRefreshToken(authentication);
		return new SigninResponseDTO(jwt, refresh);
	}

	@Override
	public AccountDTO getAccountById(Long id) {
		Optional<Account> optAccount = accountRepository.findById(id);
		Account account = optAccount.get();
		if(!optAccount.isPresent()) {
			throw new IllegalArgumentException("This id does not refer to any account.");
		}
		AccountDTO accountDTO = new AccountDTO(account);
		return accountDTO;
	}

	@Override
	public List<AccountDTO> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		List<AccountDTO> accountsDTOs = new ArrayList<>();
		for(Account account: accounts) {
			AccountDTO accountDTO = new AccountDTO(account);
			accountsDTOs.add(accountDTO);
		}
		return accountsDTOs;
	}
	
	
	
}
