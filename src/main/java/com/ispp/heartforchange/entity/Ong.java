package com.ispp.heartforchange.entity;



import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.FetchType;

import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.OngDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ong")
public class Ong extends Account{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@NotBlank
	@Size(max = 50)
	private String name;
	
	@Pattern(regexp = "([a-z]|[A-Z]|[0-9])[0-9]{7}([a-z]|[A-Z]|[0-9])")
	@Column(unique=true)
	@NotNull
	@NotBlank
	private String cif;
	
	@NotNull
	@NotBlank
	@Size(max = 250)
	private String description;
	

	@OneToMany(mappedBy="ong", cascade = CascadeType.ALL)
	private List<Person> person ;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "ong")
	private List<Grant> grants;

	
	public Ong( OngDTO ongDto ) {
		super(ongDto.getUsername(),ongDto.getEmail(), ongDto.getPassword(), ongDto.getRolAccount());
		this.name = ongDto.getName();
		this.cif = ongDto.getCif();
		this.description = ongDto.getDescription();
	}
	
}
