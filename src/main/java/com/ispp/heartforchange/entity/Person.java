package com.ispp.heartforchange.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
	


	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "person")
	private List<Attendance> attendance;
	
	
	


	
	public Person( PersonDTO personDTO ) {
		super(personDTO.getUsername(),personDTO.getEmail(), personDTO.getPassword(), personDTO.getRolAccount());
		this.id = personDTO.getId(); 
		this.name = personDTO.getName();
		this.address = personDTO.getAddress();
		this.birthday = personDTO.getBirthday();
		this.civilStatus = personDTO.getCivilStatus();
		this.documentNumber = personDTO.getDocumentNumber();
		this.documentType = personDTO.getDocumentType();
		this.driveLicenses = personDTO.getDriveLicenses();
		this.entryDate = personDTO.getEntryDate();
		this.firstSurname = personDTO.getFirstSurname();
		this.secondSurname = personDTO.getSecondSurname();
		this.gender = personDTO.getGender();
		this.leavingDate = personDTO.getLeavingDate();
		this.numberOfChildren = personDTO.getNumberOfChildren();
		this.otherSkills = personDTO.getOtherSkills();
		this.postalCode = personDTO.getPostalCode();
		this.registrationAddress = personDTO.getRegistrationAddress();
		this.telephone = personDTO.getTelephone();
		this.town = personDTO.getTown();
	}
	
	
}
