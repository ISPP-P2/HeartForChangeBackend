package com.ispp.heartforchange.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.AcademicExperience;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicExperienceDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;

	@NotNull
	@Size(max=50)
	@JsonProperty("speciality")
	private String speciality;

	@NotNull
	@Min(2023)
	@JsonProperty("endingYear")
	private Integer endingYear;

	@NotNull
	@Min(1)
	@Max(5)
	@JsonProperty("satisfactionDegree")
	private Integer satisfactionDegree;

	@NotBlank
	@NotNull
	@Size(max = 1000)
	@JsonProperty("educationalLevel")
	private String educationalLevel;

	public AcademicExperienceDTO(AcademicExperience academicExperience) {
		this.id = academicExperience.getId();
		this.speciality = academicExperience.getSpeciality();
		this.endingYear = academicExperience.getEndingYear();
		this.satisfactionDegree = academicExperience.getSatisfactionDegree();
		this.educationalLevel = academicExperience.getEducationalLevel();
	}


}
