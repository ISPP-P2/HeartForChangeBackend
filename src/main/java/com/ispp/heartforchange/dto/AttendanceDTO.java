package com.ispp.heartforchange.dto;

import java.io.Serializable;
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
import com.ispp.heartforchange.entity.GrantState;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.PetitionState;
import com.ispp.heartforchange.entity.Task;

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

	
	
	public AttendanceDTO(Attendance attendance) {
		this.id = attendance.getId();
		this.state = attendance.getState();
		this.type = attendance.getType();
	}
}
