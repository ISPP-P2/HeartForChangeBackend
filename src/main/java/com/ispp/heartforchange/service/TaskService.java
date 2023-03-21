package com.ispp.heartforchange.service;

import java.util.List; 

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.TaskDTO;

@Service
public interface TaskService {
	
	
	TaskDTO saveTask(String token,TaskDTO taskDTO);

	void deleteTask(String token, Long id);

	TaskDTO updateTask(String token, Long id, TaskDTO newTaskDTO);

	TaskDTO getById(String token, Long id);

	List<TaskDTO> getByOng(String token);
	
	List<AttendanceDTO> getAllAttendancesByTask(String token, Long id);

	List<AttendanceDTO> getAllAttendancesByVolunteer(String token, Long id);
	
	Integer getNumberOfTasks(String token);


	
}
