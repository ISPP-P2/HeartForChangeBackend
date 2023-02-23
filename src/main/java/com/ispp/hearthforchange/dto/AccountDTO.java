package com.ispp.hearthforchange.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
	
	public AccountDTO( Account account ) {
		super();
		this.id = account.getId();
		this.username = account.getUsername();
		this.password = account.getPassword();
	}

}
