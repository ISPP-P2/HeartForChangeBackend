package com.ispp.heartforchange.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Person;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class VolunteerDTO extends PersonDTO{

	private static final long serialVersionUID = 1L;

	
	@NotNull
	@NotBlank
	@Size(max = 50)
	@JsonProperty("hourOfAvailability")
	private String hourOfAvailability;
	
	@NotNull
	@JsonProperty("sexCrimes")
	private Boolean sexCrimes;
	

	public VolunteerDTO(Person person, @NotNull @NotBlank @Size(max = 50) String hourOfAvailability,
			@NotNull Boolean sexCrimes) {
		super(person);
		this.hourOfAvailability = hourOfAvailability;
		this.sexCrimes = sexCrimes;
	}



	
}
