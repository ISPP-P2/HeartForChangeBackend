package com.ispp.heartforchange.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ispp.hearthforchange.dto.AccountDTO;


@Data 
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	
	
	public Account( AccountDTO accountDto ) {
		super();
		this.id = accountDto.getId();
		this.username = accountDto.getUsername();
		this.password = accountDto.getPassword();
	}
	
	

}
