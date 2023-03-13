package com.ispp.heartforchange.dto;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.WorkExperience;
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
public class WorkExperienceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;

	@NotBlank
	@NotNull
	@Size(max = 255)
	@JsonProperty("job")
	private String job;

	@NotBlank
	@NotNull
	@Size(max = 100)
	@JsonProperty("time")
	private String time;

	@NotBlank
	@NotNull
	@Size(max = 255)
	@JsonProperty("place")
	private String place;

	@NotBlank
	@NotNull
	@Size(max = 1000)
	@JsonProperty("reasonToFinish")
	private String reasonToFinish;


	public WorkExperienceDTO(WorkExperience workExperienceSaved) {
		this.id = workExperienceSaved.getId();
		this.job = workExperienceSaved.getJob();
		this.time = workExperienceSaved.getTime();
		this.place = workExperienceSaved.getPlace();
		this.reasonToFinish = workExperienceSaved.getReasonToFinish();
	}
}