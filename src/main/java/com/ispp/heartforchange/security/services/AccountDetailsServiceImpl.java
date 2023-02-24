package com.ispp.heartforchange.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.repository.AccountRepository;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService  {
	
	  @Autowired
	  AccountRepository accountRepository;

	  @Override
	  @Transactional
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Account account = accountRepository.findByUsername(username);
	    if( account == null ) {
	    	throw new UsernameNotFoundException("User Not Found with username: " + username);
	    }
	    return AccountDetailsImpl.build(account);
	  }
}
