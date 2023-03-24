package com.ispp.heartforchange.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.Person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeneficiaryDTO extends PersonDTO {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;

	@NotNull
	@NotBlank
	@Size(max = 50)
	@JsonProperty("nationality")
	private String nationality;

	@NotNull
	@JsonProperty("doubleNationality")
	private boolean doubleNationality;

	@JsonProperty("arrivedDate")
	private LocalDate arrivedDate;

	@JsonProperty("europeanCitizenAuthorization")
	private boolean europeanCitizenAuthorization;

	@JsonProperty("touristVisa")
	private boolean touristVisa;

	@JsonProperty("dateTouristVisa")
	private LocalDate dateTouristVisa;

	@JsonProperty("healthCard")
	private boolean healthCard;

	@NotBlank
	@Size(max = 50)
	@JsonProperty("employmentSector")
	private String employmentSector;

	@NotBlank
	@Size(max = 100)
	@JsonProperty("perceptionAid")
	private String perceptionAid;

	@JsonProperty("savingsPossesion")
	private boolean savingsPossesion;

	@JsonProperty("saeInscription")
	private boolean saeInscription;

	@JsonProperty("working")
	private boolean working;

	@JsonProperty("computerKnowledge")
	private boolean computerKnowledge;

	@NotBlank
	@Size(max = 200)
	@JsonProperty("ownedDevices")
	private String ownedDevices;

	@NotBlank
	@Size(max = 100)
	@JsonProperty("languages")
	private String languages;

	public BeneficiaryDTO(Person person, Long id, @NotNull @NotBlank @Size(max = 50) String nationality,
			@NotNull boolean doubleNationality, LocalDate arrivedDate, boolean europeanCitizenAuthorization,
			boolean touristVisa, LocalDate dateTouristVisa, boolean healthCard,
			@NotBlank @Size(max = 50) String employmentSector, @NotBlank @Size(max = 100) String perceptionAid,
			boolean savingsPossesion, boolean saeInscription, boolean working, boolean computerKnowledge,
			@NotBlank @Size(max = 200) String ownedDevices, @NotBlank @Size(max = 100) String languages) {
		super(person);
		this.id = id;
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

	public BeneficiaryDTO(Beneficiary beneficiary, Person person) {
		super(person);
		this.id = beneficiary.getId();
		this.nationality = beneficiary.getNationality();
//		this.doubleNationality = beneficiary.get;
		this.arrivedDate = beneficiary.getArrivedDate();
//		this.europeanCitizenAuthorization = beneficiary;
//		this.touristVisa = beneficiary.getT;
		this.dateTouristVisa = beneficiary.getDateTouristVisa();
//		this.healthCard = beneficiary.get;
		this.employmentSector = beneficiary.getEmploymentSector();
		this.perceptionAid = beneficiary.getPerceptionAid();
//		this.savingsPossesion = beneficiary.getS;
//		this.saeInscription = saeInscription;
//		this.working = working;
//		this.computerKnowledge = computerKnowledge;
		this.ownedDevices = beneficiary.getOwnedDevices();
		this.languages = beneficiary.getLanguages();
	}

}
