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
	private boolean doubleNationality;
	
	
	private LocalDate arrivedDate;
	
	
	
	private boolean europeanCitizenAuthorization;
	
	
	
	private boolean touristVisa;
	
	
	
	private LocalDate dateTouristVisa;
	
	
	
	private boolean healthCard;
	
	
	@NotBlank
	@Size(max=50)
	private String employmentSector;
	
	
	@NotBlank
	@Size(max=100)
	private String perceptionAid;
	
	
	
	private boolean savingsPossesion;
	
	
	
	private boolean saeInscription;
	
	
	
	private boolean working;
	
	
	
	private boolean computerKnowledge;
	
	
	@NotBlank
	@Size(max=200)
	private String ownedDevices;
	
	
	@NotBlank
	@Size(max=100)
	private String languages;


	public Beneficiary(PersonDTO personDTO, @NotNull @NotBlank @Size(max = 50) String nationality,
			@NotNull boolean doubleNationality, LocalDate arrivedDate, boolean europeanCitizenAuthorization,
			boolean touristVisa, LocalDate dateTouristVisa, boolean healthCard,
			@NotBlank @Size(max = 50) String employmentSector, @NotBlank @Size(max = 100) String perceptionAid,
			boolean savingsPossesion, boolean saeInscription, boolean working, boolean computerKnowledge,
			@NotBlank @Size(max = 200) String ownedDevices, @NotBlank @Size(max = 100) String languages) {
		super(personDTO);
		this.nationality = nationality;
		this.doubleNationality = doubleNationality;
		this.arrivedDate = arrivedDate;
		this.europeanCitizenAuthorization = europeanCitizenAuthorization;
		this.touristVisa = touristVisa;
		this.dateTouristVisa = dateTouristVisa;
		this.healthCard = healthCard;
		this.employmentSector = employmentSector;
		this.perceptionAid = perceptionAid;
		this.savingsPossesion = savingsPossesion;
		this.saeInscription = saeInscription;
		this.working = working;
		this.computerKnowledge = computerKnowledge;
		this.ownedDevices = ownedDevices;
		this.languages = languages;
	}


	
	


	


	


	



}
