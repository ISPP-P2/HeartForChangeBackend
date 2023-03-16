package com.ispp.heartforchange.service;

import java.util.List;

import com.ispp.heartforchange.dto.AppointmentDTO;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;


public interface AppointmentService {

	List<AppointmentDTO> getAllAppointments();

	AppointmentDTO getAppointmentById(Long id, String token) throws OperationNotAllowedException;
	
	List<AppointmentDTO> getAppointmentsByONG(Long ongId, String token) throws OperationNotAllowedException;

	List<AppointmentDTO> getAppointmentsByBeneficiary(Long beneficiaryId, String token) throws OperationNotAllowedException;
	
	AppointmentDTO saveAppointment(String token, AppointmentDTO appointmentDTO, Long beneficiaryId) throws OperationNotAllowedException;

	AppointmentDTO updateAppointment(String token, AppointmentDTO appointmentDTO, Long appointmentId) throws OperationNotAllowedException;

	void deleteAppointment(Long id, String token) throws OperationNotAllowedException;
}
