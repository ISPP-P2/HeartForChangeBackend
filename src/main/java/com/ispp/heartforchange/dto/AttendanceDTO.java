package com.ispp.heartforchange.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.AttendanceType;
import com.ispp.heartforchange.entity.PetitionState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class AttendanceDTO implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("type")
	private AttendanceType type;
	
	
	@JsonProperty("state")
	@NotNull
	private PetitionState state;
	
	@JsonProperty("personId")
	@NotNull
	private Long personId;
	
	@JsonProperty("taskId")
	@NotNull
	private Long taskId;

	
	public AttendanceDTO(PetitionState state, Long personId,Long taskId){
		this.state = state;
		this.personId = personId;
		this.taskId = taskId;
	}

	public AttendanceDTO(Attendance attendance) {
		this.id = attendance.getId();
		this.state = attendance.getState();
		this.type = attendance.getType();
		this.personId = attendance.getPerson().getId();
		this.taskId = attendance.getTask().getId();
	}
}
