package com.ispp.heartforchange.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Appointment;
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
public class AppointmentDTO implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@JsonProperty("date")
	private LocalDate dateAppointment;
	
	@NotBlank
	@NotNull
	@Size(max = 25)
	@JsonProperty("hour")
	private String hourAppointment;
	
	@NotBlank
	@NotNull
	@Size(max = 255)
	@JsonProperty("notes")
	private String notes;

	
	public AppointmentDTO(Appointment appointment) {
		this.id = appointment.getId();
		this.dateAppointment = appointment.getDateAppointment();
		this.hourAppointment = appointment.getHourAppointment();
		this.notes = appointment.getNotes();
	}
}
