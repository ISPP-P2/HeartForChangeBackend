package com.ispp.heartforchange.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.RolAccount;

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
	
	@NotNull
	@NotBlank
	@Size(max = 20)
	@JsonProperty("username")
	private String username;
	
	@NotNull
	@NotBlank
	@Size(max = 20)
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("rolAccount")
	@NotNull
	private RolAccount rolAccount;
	
	public AccountDTO( Account account ) {
		super();
		this.id = account.getId();
		this.username = account.getUsername();
		this.password = account.getPassword();
		this.rolAccount = account.getRolAccount();
	}

}
