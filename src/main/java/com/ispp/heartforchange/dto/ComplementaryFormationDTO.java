package com.ispp.heartforchange.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.ComplementaryFormation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ComplementaryFormationDTO implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@NotBlank
	@JsonProperty("name")
	private String name;
	
	@NotNull
	@NotBlank
	@JsonProperty("organization")
	private String organization;
	

	@JsonProperty("date")
	private LocalDate date;

	@NotNull
	@NotBlank
	@JsonProperty("place")
	private String place;

	
	public ComplementaryFormationDTO(ComplementaryFormation complementaryFormationSaved) {
		this.id = complementaryFormationSaved.getId();
		this.name = complementaryFormationSaved.getName();
		this.organization = complementaryFormationSaved.getOrganization();
		this.date = complementaryFormationSaved.getDate();
		this.place = complementaryFormationSaved.getPlace();
	}
	
	
	




	
	
	





}
