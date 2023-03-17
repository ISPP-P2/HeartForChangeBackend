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
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.PersonRepository;
import com.ispp.heartforchange.repository.TaskRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService{

	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private ONGRepository ongRepository;
	private TaskRepository taskRepository;
	private PersonRepository personRepository;
	private VolunteerRepository volunteerRepository;
	private JwtUtils jwtUtils;
	
	
	/*
	 * Dependency injection 
	 */
	public TaskServiceImpl(TaskRepository volunteerRepository,
			ONGRepository ongRepository, JwtUtils jwtUtils, PersonRepository personRepository, VolunteerRepository volunterRepositor) {
		super();
		this.ongRepository = ongRepository;
		this.taskRepository = volunteerRepository;
		this.personRepository = personRepository;
		this.volunteerRepository = volunterRepositor;
		this.jwtUtils = jwtUtils;
	}
	
	
	/*
	 * Get task by id.
	 * @Param Long id
	 * @Param String username
	 * @Return TaskDTO
	 */

	@Override
	public TaskDTO getById(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Person person = personRepository.findByUsername(username);
		Optional<Task> optionalTask = taskRepository.findById(id);
		
		if(!optionalTask.isPresent()) {
			throw new UsernameNotFoundException("This Task doesn't exist");
		}else {
			if(ong != null && optionalTask.get().getOng().getId() == ong.getId()) {				
				Task task = optionalTask.get();
				TaskDTO tasktDTO = new TaskDTO(task);
				return tasktDTO;
			}else if(person != null && person.getRolAccount() == RolAccount.VOLUNTEER && optionalTask.get().getOng().getId() == person.getOng().getId()) {
				Task task = optionalTask.get();
				TaskDTO tasktDTO = new TaskDTO(task);
				return tasktDTO;
			}else throw new UsernameNotFoundException("Dont have the permisions");
		}
	}
	
	/*
	 * Get all task by Ong.
	 * @Param String ongName
	 * @Return List<TaskDTO>
	 */

	@Override
    public List<TaskDTO> getByOng(String token, Long id) {

        //Identify if user logged is an ong or a volunteer
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Ong loggedOng = ongRepository.findByUsername(username);
        Volunteer loggedVolunteer =volunteerRepository.findByUsername(username);

        // ONG searched
        Optional<Ong> ong = ongRepository.findById(id);

        if(ong.isPresent()) {      	
        	// Tasks of the ONG
        	List<Task> tasks = taskRepository.findByOng(ong.get());
        	List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
        	
        	if(loggedOng != null) {
        		// Logged as ong
        		if(ong.get().equals(loggedOng)) {
        			for(Task task : tasks) {
        				TaskDTO tasktDTO = new TaskDTO(task);
        				tasksDTO.add(tasktDTO);
        			}
        		}else {
        			throw new UsernameNotFoundException("You cannot get information about an ONG which is not yours.");
        		}
        	}else if(loggedVolunteer != null){
        		// Logged as volunteer
        		if(loggedVolunteer.getOng().equals(ong.get())) {
        			for(Task task : tasks) {
        				if(task.getType()==TaskType.ACTIVIDAD) {
        					TaskDTO tasktDTO = new TaskDTO(task);
        					tasksDTO.add(tasktDTO);
        				}
        			}
        		}else {
        			throw new UsernameNotFoundException("You cannot get information about an ONG which you do not belong to.");
        		}
        	}else {
        		throw new UsernameNotFoundException("You must be logged to use this method.");
        	}
        	
        	logger.info("Trying to get the Tasks in the Ong with name={}", ong.get().getName());
        	return tasksDTO;
        }
        throw new UsernameNotFoundException("There is not an Ong with that id= " + id);
    }

	/*
	 * Save task.
	 * @Param TaskDTO taskDTO
	 * @Param String username
	 * @Return TaskDTO
	 */
	@Override
	public TaskDTO saveTask(TaskDTO taskDTO, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Task newTask = new Task(taskDTO, ong);
		newTask.setId(Long.valueOf(0));
		logger.info("Starting to Save Task with name={}", newTask.getName());
		try {
			logger.info("Saving Task with name={}", newTask.getName());
			Task taskSave = taskRepository.save(newTask);
			logger.info("Saving Task with name={}", newTask.getName());
			return new TaskDTO(taskSave);
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	/*
	 * Update task.
	 * @Param TaskDTO taskDTO
	 * @Param String username
	 * @Return TaskDTO
	 */
	
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
			throw new UsernameNotFoundException("You cannot update a task which does not belong to you.");
		}
		throw new UsernameNotFoundException("This Task doesn't exist");
		
	}
	
	/*
	 * Delete task.
	 * @Param Long id
	 * @Param String token
	 * @Return TaskDTO
	 */
	
	
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
				throw new UsernameNotFoundException("You cannot delete a task which does not belong to you.");
			}

		} else {
			throw new UsernameNotFoundException("This Task does not exist!");
		}
	}

}
