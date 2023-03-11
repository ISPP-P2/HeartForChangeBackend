package com.ispp.heartforchange.service;

import java.util.List;

import com.ispp.heartforchange.dto.AppointmentDTO;


public interface AppointmentService {

	List<AppointmentDTO> getAllAppointments();

	AppointmentDTO getAppointmentById(Long id, String token);
	
	List<AppointmentDTO> getAppointmentsByONG(String ongUsername, String token);

	AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO, String token);

	AppointmentDTO updateAppointment(String token, AppointmentDTO appointmentDTO);

	void deleteAppointment(Long id, String token);
}
