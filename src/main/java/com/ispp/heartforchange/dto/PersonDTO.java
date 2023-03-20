package com.ispp.heartforchange.dto;

import java.time.LocalDate;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Person;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class PersonDTO extends AccountDTO{

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@JsonProperty("entryDate")
	private LocalDate entryDate;
	
	
	@NotNull
	@NotBlank
	@Size(max=20)
	@JsonProperty("firstSurname")
	private String firstSurname;
	
	@NotNull
	@NotBlank
	@Size(max=20)
	@JsonProperty("secondSurname")
	private String secondSurname;
	
	@NotNull
	@NotBlank
	@Size(max=20)
	@JsonProperty("name")
	private String name;
	
	@NotNull
	@JsonProperty("documentType")
	private DocumentType documentType;
	
	@NotNull
	@NotBlank
	@Size(min=9, max=9)
	@JsonProperty("documentNumber")
	private String documentNumber;
	
	@NotNull
	@JsonProperty("gender")
	private Gender gender;
	
	@NotNull
	@JsonProperty("birthday")
	private LocalDate birthday;
	
	@NotNull
	@JsonProperty("civilStatus")
	private CivilStatus civilStatus;
	
	@NotNull
	@Min(0)
	@JsonProperty("numberOfChildren")
	private int numberOfChildren;
	
	@NotNull
	@NotBlank
	@Size(max=50)
	@JsonProperty("address")
	private String address;
	
	@NotNull
	@NotBlank
	@Size(max=10)
	@JsonProperty("postalCode")
	private String postalCode;
	
	@Size(max=50)
	@JsonProperty("registrationAddress")
	private String registrationAddress;
	
	@NotNull
	@NotBlank
	@Size(max=20)
	@JsonProperty("town")
	private String town;
	
	@NotNull
	@NotBlank
	@Size(max=15)
	@JsonProperty("telephone")
	private String telephone;
	
	@JsonProperty("leavingDate")
	private LocalDate leavingDate;
	
	@NotNull
	@NotBlank
	@Size(max=100)
	@JsonProperty("driveLicenses")
	private String driveLicenses;
	
	@NotNull
	@NotBlank
	@Size(max=350)
	@JsonProperty("otherSkills")
	private String otherSkills;

	

	public PersonDTO( Person person ) {
		super(person.getUsername(),person.getEmail(), person.getPassword());
		this.id = person.getId(); 
		this.name = person.getName();
		this.address = person.getAddress();
		this.birthday = person.getBirthday();
		this.civilStatus = person.getCivilStatus();
		this.documentNumber = person.getDocumentNumber();
		this.documentType = person.getDocumentType();
		this.driveLicenses = person.getDriveLicenses();
		this.entryDate = person.getEntryDate();
		this.firstSurname = person.getFirstSurname();
		this.secondSurname = person.getSecondSurname();
		this.gender = person.getGender();
		this.leavingDate = person.getLeavingDate();
		this.numberOfChildren = person.getNumberOfChildren();
		this.otherSkills = person.getOtherSkills();
		this.postalCode = person.getPostalCode();
		this.registrationAddress = person.getRegistrationAddress();
		this.telephone = person.getTelephone();
		this.town = person.getTown();
	}



}
