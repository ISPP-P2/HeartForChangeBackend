package com.ispp.heartforchange.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TaskDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@NotBlank
	@Size(max = 32)
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("type")
	@NotNull
	private TaskType type;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("date")
	@NotNull
	private LocalDateTime date;   // fecha y hora
	
	@JsonProperty("teacher")
	@NotNull
	private String teacher;
	
	@JsonProperty("certificate")
	@NotNull
	private Boolean certificate;
	
	@JsonProperty("observations")
	@Length(max = 256)
	private String observations;
	
	@JsonProperty("incidences")
	private String incidences;
	
	@JsonProperty("coordinator")
	@NotNull
	private String coordinator;
	
	@JsonProperty("numParticipants")
	private Integer numParticipants;
	
	@JsonProperty("place")
	@NotNull
	private String place;
	
	
	public TaskDTO(Task task) {
		this.id = task.getId();
		this.name = task.getName();
		this.date = task.getDate();
		this.teacher = task.getTeacher();
		this.certificate = task.getCertificate();
		this.observations = task.getObservations();
		this.incidences = task.getIncidences();
		this.coordinator = task.getCoordinator();
		this.numParticipants = task.getNumParticipants();
		this.type = task.getType();
		this.place = task.getPlace();
	}
	



	public TaskDTO(Long id, @NotNull @NotBlank @Size(max = 32) String name, 	@NotNull LocalDateTime date, @NotNull String teacher,
			@NotNull Boolean certificate, @Length(max = 256) String observations, @Length(max = 256) String incidences,
			@NotNull String coordinator, Integer numParticipants, @NotNull TaskType taskType, @NotNull String place) {
		
		this.id = id;
		this.name = name;
		this.date = date;
		this.teacher = teacher;
		this.certificate = certificate;
		this.observations = observations;
		this.incidences = incidences;
		this.coordinator = coordinator;
		this.numParticipants = numParticipants;
		this.type = taskType;
		this.place = place;
	}
}
