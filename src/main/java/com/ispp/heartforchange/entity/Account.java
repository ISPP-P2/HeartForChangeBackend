package com.ispp.heartforchange.entity;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ispp.heartforchange.dto.AccountDTO;


@Data 
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account", 
	uniqueConstraints = { 
	  @UniqueConstraint(columnNames = "username"),
	})
public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@NotBlank
	@Size(max = 20)
	private String username;
	
	@NotNull
	@NotBlank
	@Size(max = 120)
	private String password;
	
	@NotNull
	private RolAccount rolAccount;
	
	
	public Account( AccountDTO accountDto ) {
		super();
		this.id = accountDto.getId();
		this.username = accountDto.getUsername();
		this.password = accountDto.getPassword();
		this.rolAccount = accountDto.getRolAccount();
	}
	
	

}
