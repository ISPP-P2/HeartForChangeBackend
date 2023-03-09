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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.ispp.heartforchange.dto.WorkExperienceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_experience")
public class WorkExperience implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@NotNull
	@Size(max = 255)
	private String job;

	@NotBlank
	@NotNull
	@Size(max = 100)
	private String time;

	@NotBlank
	@NotNull
	@Size(max = 255)
	private String place;

	@NotBlank
	@NotNull
	@Size(max = 1000)
	private String reasonToFinish;


	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "volunteer_id")
	private Volunteer volunteer;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "beneficiary_id")
	private Beneficiary beneficiary;
	

	public WorkExperience(WorkExperienceDTO workExperienceDTO) {
		this.id = workExperienceDTO.getId();
		this.job = workExperienceDTO.getJob();
		this.time = workExperienceDTO.getTime();
		this.place = workExperienceDTO.getPlace();
		this.reasonToFinish = workExperienceDTO.getReasonToFinish();
	}

}
