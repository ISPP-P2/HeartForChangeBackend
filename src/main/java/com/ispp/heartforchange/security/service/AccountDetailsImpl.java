package com.ispp.heartforchange.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ispp.heartforchange.entity.Account;

public class AccountDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	  private Long id;

	  private String username;

	  @JsonIgnore
	  private String password;
	  

	  private Collection<? extends GrantedAuthority> authorities;

	  public AccountDetailsImpl(Long id, String username, String password,
	      Collection<? extends GrantedAuthority> authorities) {
	    this.id = id;
	    this.username = username;
	    this.password = password;
	    this.authorities = authorities;
	  }

	  public static AccountDetailsImpl build(Account account) {
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    authorities.add( new SimpleGrantedAuthority(account.getRolAccount().toString() ));
	    return new AccountDetailsImpl(
	    	account.getId(), 
	    	account.getUsername(), 
	    	account.getPassword(), 
	        authorities);
	  }

	  @Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
	    return authorities;
	  }

	  public Long getId() {
	    return id;
	  }

	  @Override
	  public String getPassword() {
	    return password;
	  }

	  @Override
	  public String getUsername() {
	    return username;
	  }

	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }

	  @Override
	  public boolean equals(Object o) {
	    if (this == o)
	      return true;
	    if (o == null || getClass() != o.getClass())
	      return false;
	    AccountDetailsImpl user = (AccountDetailsImpl) o;
	    return Objects.equals(id, user.id);
	  }
}
