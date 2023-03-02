package com.ispp.heartforchange.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.OngDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ong")
public class Ong extends Account{
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@NotBlank
	@Size(max = 50)
	private String name;
	
	@NotNull
	private String cif;
	
	@NotNull
	@NotBlank
	@Size(max = 250)
	private String description;
	
	public Ong( OngDTO ongDto ) {
		super(ongDto.getUsername(), ongDto.getPassword(), ongDto.getRolAccount());
		this.id = ongDto.getId();
		this.name = ongDto.getName();
		this.cif = ongDto.getCif();
		this.description = ongDto.getDescription();
	}
	
}
