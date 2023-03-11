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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.AppointmentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment")
public class Appointment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private LocalDate dateAppointment;
	
	@NotBlank
	@NotNull
	@Size(max = 25)
	private String hourAppointment;
	
	@NotBlank
	@NotNull
	@Size(max = 255)
	private String notes;
	
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ong_id")
	private Ong ong;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "beneficiary_id")
	private Beneficiary beneficiary;
	

	public Appointment(AppointmentDTO appointmentDTO) {
		this.id = appointmentDTO.getId();
		this.dateAppointment = appointmentDTO.getDateAppointment();
		this.hourAppointment = appointmentDTO.getHourAppointment();
		this.notes = appointmentDTO.getNotes();
	}
}
