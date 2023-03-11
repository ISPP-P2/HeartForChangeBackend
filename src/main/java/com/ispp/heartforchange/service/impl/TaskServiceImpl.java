package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.TaskRepository;
import com.ispp.heartforchange.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService{

	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	private TaskRepository taskRepository;
	private JwtUtils jwtUtils;
	
	
	/*
	 * Dependency injection 
	 */
	public TaskServiceImpl(TaskRepository volunteerRepository,
			ONGRepository ongRepository, JwtUtils jwtUtils) {
		super();
		this.ongRepository = ongRepository;
		this.taskRepository = volunteerRepository;
		this.jwtUtils = jwtUtils;
	}
	
	
	@Override
	public List<TaskDTO> getAll() {
		List<Task> tasks = taskRepository.findAll();
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
		for(Task task : tasks) {
			TaskDTO tasktDTO = new TaskDTO(task.getId(),
					task.getName(),
					task.getDate(),
					task.getTeacher(),
					task.getCertificate(),
					task.getObservations(),
					task.getIncidences(),
					task.getCoordinator(),
					task.getNumParticipants(),
					task.getType(),
					task.getPlace());
			tasksDTO.add(tasktDTO);
		}
		return tasksDTO;
	}

	@Override
	public TaskDTO getById(Long id, String username) {
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> optionalTask = taskRepository.findById(id);
		
		if(!optionalTask.isPresent()) {
			throw new UsernameNotFoundException("This Task doesn't exist");
		}else {
			if(optionalTask.get().getOng().getId() == ong.getId()) {				
				Task task = optionalTask.get();
				TaskDTO tasktDTO = new TaskDTO(task.getId(),
						task.getName(),
						task.getDate(),
						task.getTeacher(),
						task.getCertificate(),
						task.getObservations(),
						task.getIncidences(),
						task.getCoordinator(),
						task.getNumParticipants(),
						task.getType(),
						task.getPlace());
				return tasktDTO;
			}
			throw new UsernameNotFoundException("Dont have the permisions");
		}
	}

	@Override
	public List<TaskDTO> getByOng(String ongName) {
		Ong ong = ongRepository.findByUsername(ongName);
		logger.info("Trying to get the Tasks in the Ong with name={}", ongName);
		List<Task> tasks = taskRepository.findByOng(ong);
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
		for(Task task : tasks) {
			TaskDTO tasktDTO = new TaskDTO(task.getId(),
					task.getName(),
					task.getDate(),
					task.getTeacher(),
					task.getCertificate(),
					task.getObservations(),
					task.getIncidences(),
					task.getCoordinator(),
					task.getNumParticipants(),
					task.getType(),
					task.getPlace());
			tasksDTO.add(tasktDTO);
		}
		return tasksDTO;
	}

	@Override
	public TaskDTO saveTask(TaskDTO taskDTO, String username) {
		Ong ong = ongRepository.findByUsername(username);
		Task newTask = new Task(taskDTO, ong);
		newTask.setId(Long.valueOf(0));
		logger.info("Starting to Save Task with name={}", newTask.getName());
		try {
			logger.info("Saving Task with name={}", newTask.getName());
			Task taskSave = taskRepository.save(newTask);
			logger.info("Saving Task with name={}", newTask.getName());
			return new TaskDTO(taskSave.getId(),
					taskSave.getName(),
					taskSave.getDate(),
					taskSave.getTeacher(),
					taskSave.getCertificate(),
					taskSave.getObservations(),
					taskSave.getIncidences(),
					taskSave.getCoordinator(),
					taskSave.getNumParticipants(),
					taskSave.getType(),
					taskSave.getPlace());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	@Override
	public TaskDTO updateTask(Long id, TaskDTO newTaskDTO , String username) {
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> taskToUpdate = taskRepository.findById(id);
		logger.info("Task is updating with id={}", id);
		if(taskToUpdate.isPresent()) {
			if(taskToUpdate.get().getOng().getId() == ong.getId()) {
				
				taskToUpdate.get().setCertificate(newTaskDTO.getCertificate());
				taskToUpdate.get().setCoordinator(newTaskDTO.getCoordinator());
				taskToUpdate.get().setDate(newTaskDTO.getDate());
				taskToUpdate.get().setIncidences(newTaskDTO.getIncidences());
				taskToUpdate.get().setName(newTaskDTO.getName());
				taskToUpdate.get().setNumParticipants(newTaskDTO.getNumParticipants());
				taskToUpdate.get().setObservations(newTaskDTO.getObservations());
				taskToUpdate.get().setPlace(newTaskDTO.getPlace());
				taskToUpdate.get().setTeacher(newTaskDTO.getTeacher());
				taskToUpdate.get().setOng(ong);
				Task taskSave = taskRepository.save(taskToUpdate.get());
				newTaskDTO.setId(taskSave.getId());
				return newTaskDTO;
			}
			throw new UsernameNotFoundException("Dont have the permisions");
		}
		throw new UsernameNotFoundException("This Task doesn't exist");
		
	}
	
	
	@Override
	public void deleteTask(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(id);
		if (task.isPresent()) {
			if (task.get().getOng().getId() == ong.getId()) {
				System.out.println(task);
				taskRepository.delete(task.get());
			} else {
				throw new UsernameNotFoundException("Error deleting task");
			}

		} else {
			throw new UsernameNotFoundException("This Task not exist!");
		}
	}

}
