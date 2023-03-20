package com.ispp.heartforchange.dto;

import java.io.Serializable;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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
	@Size(max = 200)
	@JsonProperty("email")
	private String email;
	
	@NotNull
	@NotBlank
	@Size(max = 20)
	@JsonProperty("username")
	private String username;
	
	@NotNull
	@NotBlank
	@Size(max = 120)
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("rolAccount")
	private RolAccount rolAccount;		

	
	public AccountDTO( Account account ) {
		super();
		this.id = account.getId();
		this.username = account.getUsername();
		this.password = account.getPassword();
		this.rolAccount = account.getRolAccount();
	}

	public AccountDTO(@NotNull @NotBlank @Size(max = 20) String username,
			@NotNull @NotBlank @Size(max = 20) String email,
			@NotNull @NotBlank @Size(max = 120) String password, @NotNull RolAccount rolAccount) {
		super();
		this.username = username;
		this.password = password;
		this.rolAccount = rolAccount;
		this.email = email;
	}

}
