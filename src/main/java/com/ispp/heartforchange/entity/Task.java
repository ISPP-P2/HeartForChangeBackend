package com.ispp.heartforchange.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
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

import org.hibernate.validator.constraints.Length;

import com.ispp.heartforchange.dto.TaskDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "task")
public class Task implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private TaskType type;
	
	@NotNull
	@NotBlank
	@Size(max=32)
	private String name;
	
	@NotNull
	private LocalDateTime date;   // fecha y hora
	
	@NotNull
	private String teacher;
	
	@NotNull
	private Boolean certificate;
	
	@Length(max = 256)
	private String observations;
	
	@Length(max = 256)
	private String incidences;
	
	@NotNull
	private String coordinator;
	
	private Integer numParticipants;
	
	@NotNull
	private String place;
	
//	
//	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "task")
//	private List<Attendance> attendance;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ong_id")
	private Ong ong;

	public Task(TaskDTO taskDTO, Ong ong) {
		
		this.name = taskDTO.getName();
		this.date = taskDTO.getDate();
		this.teacher = taskDTO.getTeacher();
		this.certificate = taskDTO.getCertificate();
		this.observations = taskDTO.getObservations();
		this.incidences = taskDTO.getIncidences();
		this.coordinator = taskDTO.getCoordinator();
		this.numParticipants = taskDTO.getNumParticipants();
		this.place = taskDTO.getPlace();
		this.type = taskDTO.getType();
		this.ong = ong;
	}
	
	
}
