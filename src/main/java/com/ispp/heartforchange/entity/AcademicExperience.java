package com.ispp.heartforchange.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.AcademicExperienceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academic_experience")
public class AcademicExperience implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(max=50)
	private String speciality;

	@NotNull
	private Integer endingYear;

	@NotNull
	@Min(1)
	@Max(5)
	private Integer satisfactionDegree;

	@NotBlank
	@NotNull
	@Size(max = 1000)
	private String educationalLevel;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "beneficiary_id")
	private Beneficiary beneficiary;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "volunteer_id")
	private Volunteer volunteer;

	public AcademicExperience(AcademicExperienceDTO academicExperienceDTO) {
		
		this.id = academicExperienceDTO.getId();
		this.speciality = academicExperienceDTO.getSpeciality();
		this.endingYear = academicExperienceDTO.getEndingYear();
		this.satisfactionDegree = academicExperienceDTO.getSatisfactionDegree();
		this.educationalLevel = academicExperienceDTO.getEducationalLevel();
	}


}
