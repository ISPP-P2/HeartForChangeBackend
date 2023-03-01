package com.ispp.heartforchange.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.AccountDTO;
import com.ispp.heartforchange.dto.ONGDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ong", 
	uniqueConstraints = { 
	  @UniqueConstraint(columnNames = "cif"),
	})
public class ONG implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private Account account;
	
	@NotNull
	@NotBlank
	@Size(max = 50)
	private String name;
	
	@NotNull
	@Pattern(regexp="^([ABCDEFGHPQSKLMX])(\\d{7})([0-9A-J])$",message="CIF invalid, try again")
	private String cif;
	
	@NotNull
	@NotBlank
	@Size(max = 250)
	private String description;
	
	public ONG( ONGDTO ongDto ) {
		super();
		this.id = ongDto.getId();
		this.account = ongDto.getAccount();
		this.name = ongDto.getName();
		this.cif = ongDto.getCif();
		this.description = ongDto.getDescription();
	}
	
}
