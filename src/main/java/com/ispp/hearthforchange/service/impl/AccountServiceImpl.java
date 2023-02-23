package com.ispp.hearthforchange.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.entity.Account;
import com.ispp.hearthforchange.dto.AccountDTO;
import com.ispp.hearthforchange.repository.AccountRepository;
import com.ispp.hearthforchange.service.AccountService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {
	
	private AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public AccountDTO createAccount(AccountDTO accountDto) {
		accountDto.setPassword( passwordEncoder.encode(accountDto.getPassword()));
		Account account = accountRepository.save( new Account( accountDto ) );
		return new AccountDTO( account );
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username);
		if( account == null ) {
			throw new UsernameNotFoundException("Account not found in database");
		} 
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		return new org.springframework.security.core.userdetails.User( account.getUsername(), account.getPassword(), authorities);
	}
	

}
