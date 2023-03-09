package com.ispp.heartforchange.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.AttendanceType;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.PetitionState;
import com.ispp.heartforchange.entity.Task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttendanceDTO {

	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("type")
	private AttendanceType type;
	
	@NotNull
	@JsonProperty("person")
	private Person person;
	
	@NotNull
	@JsonProperty("task")
	private Task task;
	
	@JsonProperty("state")
	@NotNull
	private PetitionState state;
	
	
	public AttendanceDTO(Attendance attendance) {
		this.id = attendance.getId();
		this.person = attendance.getPerson();
		this.state = attendance.getState();
		this.task = attendance.getTask();
		this.type = attendance.getType();
	}
}
