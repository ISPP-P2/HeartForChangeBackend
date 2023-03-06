package com.ispp.heartforchange.entity;

import java.time.LocalDate; 

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.ispp.heartforchange.dto.PersonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person")
public class Person extends Account{
	
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private LocalDate entryDate;
	
	@NotNull
	@NotBlank
	@Size(max=20)
	private String firstSurname;
	
	@NotNull
	@NotBlank
	@Size(max=20)
	private String secondSurname;
	
	@NotNull
	@NotBlank
	@Size(max=20)
	private String name;
	
	@NotNull
	private DocumentType documentType;
	
	@NotNull
	@NotBlank
	@Size(min=9, max=9)
	private String documentNumber;
	
	@NotNull
	private Gender gender;
	
	@NotNull
	private LocalDate birthday;
	
	@NotNull
	private CivilStatus civilStatus;
	
	@NotNull
	@Min(0)
	private int numberOfChildren;
	
	@NotNull
	@NotBlank
	@Size(max=50)
	private String address;
	
	@NotNull
	@NotBlank
	@Size(max=10)
	private String postalCode;
	
	@Size(max=50)
	private String registrationAddress;
	
	@NotNull
	@NotBlank
	@Size(max=20)
	private String town;
	
	@NotNull
	@NotBlank
	@Size(max=15)
	private String telephone;
	
	private LocalDate leavingDate;
	
	@NotNull
	@NotBlank
	@Size(max=100)
	private String driveLicenses;
	
	@NotNull
	@NotBlank
	@Size(max=350)
	private String otherSkills;

	@ManyToOne
	private Ong ong;
	
	public Person( PersonDTO personDto ) {
		super(personDto.getUsername(),personDto.getEmail(), personDto.getPassword(), personDto.getRolAccount());
		this.id = personDto.getId();
		this.name = personDto.getName();
		this.address = personDto.getAddress();
		this.birthday = personDto.getBirthday();
		this.civilStatus = personDto.getCivilStatus();
		this.documentNumber = personDto.getDocumentNumber();
		this.documentType = personDto.getDocumentType();
		this.driveLicenses = personDto.getDriveLicenses();
		this.entryDate = personDto.getEntryDate();
		this.firstSurname = personDto.getFirstSurname();
		this.secondSurname = personDto.getSecondSurname();
		this.gender = personDto.getGender();
		this.leavingDate = personDto.getLeavingDate();
		this.numberOfChildren = personDto.getNumberOfChildren();
		this.otherSkills = personDto.getOtherSkills();
		this.postalCode = personDto.getPostalCode();
		this.registrationAddress = personDto.getRegistrationAddress();
		this.telephone = personDto.getTelephone();
		this.town = personDto.getTown();
	}

	
	
	
	

}
