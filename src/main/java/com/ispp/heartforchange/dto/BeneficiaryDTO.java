package com.ispp.heartforchange.dto;

import java.time.LocalDate;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BeneficiaryDTO extends PersonDTO{
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@NotBlank
	@Size(max = 50)
	@JsonProperty("nationality")
	private String nationality;
	
	@NotNull
	@JsonProperty("doublenationality")
	private boolean doublenationality;
	
	
	@JsonProperty("arrived_date")
	private LocalDate arrived_date;
	
	
	
	@JsonProperty("european_citizen_authorization")
	private boolean european_citizen_authorization;
	
	
	
	@JsonProperty("tourist_visa")
	private boolean tourist_visa;
	
	
	
	@JsonProperty("date_tourist_visa")
	private LocalDate date_tourist_visa;
	
	
	
	@JsonProperty("health_card")
	private boolean health_card;
	
	
	@NotBlank
	@Size(max=50)
	@JsonProperty("employment_sector")
	private String employment_sector;
	
	
	@NotBlank
	@Size(max=100)
	@JsonProperty("perception_aid")
	private String perception_aid;
	
	
	
	@JsonProperty("savings_possesion")
	private boolean savings_possesion;
	
	
	
	@JsonProperty("sae_inscription")
	private boolean sae_inscription;
	
	
	
	@JsonProperty("working")
	private boolean working;
	
	
	
	@JsonProperty("computer_knowledge")
	private boolean computer_knowledge;
	
	
	@NotBlank
	@Size(max=200)
	@JsonProperty("owned_devices")
	private String owned_devices;
	
	
	@NotBlank
	@Size(max=100)
	@JsonProperty("languages")
	private String languages;


	public BeneficiaryDTO(Person person, Long id, @NotNull @NotBlank @Size(max = 50) String nationality,
			@NotNull boolean doublenationality,  LocalDate arrived_date, boolean european_citizen_authorization,
			boolean tourist_visa, LocalDate date_tourist_visa, boolean health_card,
			@NotBlank @Size(max = 50) String employment_sector, @NotBlank @Size(max = 100) String perception_aid,
			boolean savings_possesion, boolean sae_inscription, boolean working, boolean computer_knowledge,
			@NotBlank @Size(max = 200) String owned_devices, @NotBlank @Size(max = 100) String languages) {
		super(person);
		this.id = id;
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
