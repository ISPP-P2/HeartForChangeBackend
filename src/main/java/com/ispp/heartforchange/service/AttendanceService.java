package com.ispp.heartforchange.service;

import java.util.List;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.entity.AttendanceType;

public interface AttendanceService {

	List<AttendanceDTO> getAll();

	AttendanceDTO createPetition(Long id, String token);

	void deletePetition(Long id, String token);

	AttendanceDTO acceptPetition(Long id, String token);

	AttendanceDTO denyPetition(Long id, String token);

	AttendanceDTO confirmAttendance(String token, AttendanceType type, Long id);

	AttendanceDTO addBeneficiary(Long idTask, String token, Long idPerson);

	void deleteBeneficiary(Long idTask, String token, Long idPerson);

	AttendanceDTO getAttendanceById(Long id, String token);
	
}
