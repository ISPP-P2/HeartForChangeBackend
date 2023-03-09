package com.ispp.heartforchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.entity.Ong;

@Service
public interface TaskService {
	
	List<TaskDTO> getAll();

	
	List<TaskDTO> getByOng(String ongName);
	
	TaskDTO saveTask(TaskDTO taskDTO, String ongName);


	void deleteTask(Long id, String token);

	TaskDTO updateTask(Long id, TaskDTO newTaskDTO, String username);

	TaskDTO getById(Long id, String token);
	
}
