package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;

@Service
public interface TaskService {

	TaskDTO getById(String token, Long id);

	List<TaskDTO> getByOng(String token);

	List<AttendanceDTO> getAllAttendancesOngByVolunteer(String token, Long id);

	Integer getNumberOfTasks(String token);

	TaskDTO saveActivity(String token, TaskDTO taskDTO);

	TaskDTO saveCurso(String token, TaskDTO taskDTO);

	TaskDTO saveTaller(String token, TaskDTO taskDTO);

	TaskDTO updateActivity(String token, Long id, TaskDTO newTaskDTO);

	TaskDTO updateCurso(String token, Long id, TaskDTO newTaskDTO);

	TaskDTO updateTaller(String token, Long id, TaskDTO newTaskDTO);

	void deleteActivity(String token, Long id);

	void deleteCurso(String token, Long id);

	void deleteTaller(String token, Long id);

	List<TaskDTO> getActivityByOng(String token);

	List<TaskDTO> getTallerByOng(String token);

	List<TaskDTO> getCursoByOng(String token);

	List<AttendanceDTO> getAllAttendancesVolunteerByVolunteer(String token);

	List<VolunteerDTO> getAllVoluntariesByTask(String token, Long id);

	List<BeneficiaryDTO> getAllBeneficiariesByTask(String token, Long id);
	
	List<VolunteerDTO> getPetitionsByTask(String token, Long id);

}
