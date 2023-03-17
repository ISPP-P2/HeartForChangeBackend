package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.TaskDTO;

@Service
public interface TaskService {
	
	
	TaskDTO saveTask(TaskDTO taskDTO, String ongName);


	void deleteTask(Long id, String token);

	TaskDTO updateTask(Long id, TaskDTO newTaskDTO, String username);

	TaskDTO getById(Long id, String token);

	List<TaskDTO> getByOng(String token, Long id);


	
}
