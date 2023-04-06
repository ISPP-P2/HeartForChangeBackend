package com.ispp.heartforchange.entity;


import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "attendance")
public class Attendance implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	private AttendanceType type;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "person_id")
	private Person person;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "task_id")
	private Task task;
	
	@NotNull
	@DateTimeFormat
	private LocalDate date;
	
	@NotNull
	private PetitionState state;

	public Attendance(@NotNull Person person, @NotNull Task task) {
		super();
		this.state = PetitionState.ESPERA;
		this.person = person;
		this.task = task;
		this.date = LocalDate.now();
	}
	
	public Attendance(@NotNull Person person, @NotNull Task task, @NotNull PetitionState state) {
		super();
		this.state = state;
		this.person = person;
		this.task = task;
		this.date = LocalDate.now();
	}
	
}
