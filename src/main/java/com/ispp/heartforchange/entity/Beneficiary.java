package com.ispp.heartforchange.entity;

import java.time.LocalDate;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.PersonDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "beneficiary")
public class Beneficiary extends Person{
	
private static final long serialVersionUID = 1L;
	
	
	@NotNull
	@NotBlank
	@Size(max = 50)
	private String nationality;
	
	@NotNull
	private boolean doublenationality;
	
	
	private LocalDate arrived_date;
	
	
	
	private boolean european_citizen_authorization;
	
	
	
	private boolean tourist_visa;
	
	
	
	private LocalDate date_tourist_visa;
	
	
	
	private boolean health_card;
	
	
	@NotBlank
	@Size(max=50)
	private String employment_sector;
	
	
	@NotBlank
	@Size(max=100)
	private String perception_aid;
	
	
	
	private boolean savings_possesion;
	
	
	
	private boolean sae_inscription;
	
	
	
	private boolean working;
	
	
	
	private boolean computer_knowledge;
	
	
	@NotBlank
	@Size(max=200)
	private String owned_devices;
	
	
	@NotBlank
	@Size(max=100)
	private String languages;


	public Beneficiary(PersonDTO personDTO, @NotNull @NotBlank @Size(max = 50) String nationality,
			@NotNull boolean doublenationality,  LocalDate arrived_date, boolean european_citizen_authorization,
			boolean tourist_visa, LocalDate date_tourist_visa, boolean health_card,
			@NotBlank @Size(max = 50) String employment_sector, @NotBlank @Size(max = 100) String perception_aid,
			boolean savings_possesion, boolean sae_inscription, boolean working, boolean computer_knowledge,
			@NotBlank @Size(max = 200) String owned_devices, @NotBlank @Size(max = 100) String languages) {
		super(personDTO);
		this.nationality = nationality;
		this.doublenationality = doublenationality;
		this.arrived_date = arrived_date;
		this.european_citizen_authorization = european_citizen_authorization;
		this.tourist_visa = tourist_visa;
		this.date_tourist_visa = date_tourist_visa;
		this.health_card = health_card;
		this.employment_sector = employment_sector;
		this.perception_aid = perception_aid;
		this.savings_possesion = savings_possesion;
		this.sae_inscription = sae_inscription;
		this.working = working;
		this.computer_knowledge = computer_knowledge;
		this.owned_devices = owned_devices;
		this.languages = languages;
	}


	


	


	


	



}
