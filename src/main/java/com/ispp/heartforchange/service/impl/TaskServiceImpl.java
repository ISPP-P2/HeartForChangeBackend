package com.ispp.heartforchange.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.entity.Attendance;
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
	 * @Param String token
	 * @Param Long id
	 * @Return TaskDTO
	 */

	@Override
	public TaskDTO getById(String token, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Person person = personRepository.findByUsername(username);
		Optional<Task> optionalTask = taskRepository.findById(id);
		
		if(!optionalTask.isPresent()) {
			throw new IllegalArgumentException("This task does not exist!");
		}else {
			if(ong != null && optionalTask.get().getOng().getId() == ong.getId()) {				
				Task task = optionalTask.get();
				TaskDTO tasktDTO = new TaskDTO(task);
				return tasktDTO;
			}else if(person != null && person.getRolAccount() == RolAccount.VOLUNTEER && optionalTask.get().getOng().getId() == person.getOng().getId()) {
				Task task = optionalTask.get();
				TaskDTO tasktDTO = new TaskDTO(task);
				return tasktDTO;
			}else throw new UsernameNotFoundException("You must be logged as ONG or volunteer to make this operation.");
		}
	}
	
	/*
	 * Get all task by Ong.
	 * @Param String token
	 * @Param Long id
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
        		throw new UsernameNotFoundException("You must be logged to make this operation.");
        	}
        	
        	logger.info("Trying to get the tasks from ONG with name={}", ong.get().getName());
        	return tasksDTO;
        }
        throw new IllegalArgumentException("There is not an ONG with id=" + id);
    }

	/*
	 * Save task.
	 * @Param String token
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	@Override
	public TaskDTO saveTask(String token, TaskDTO taskDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Task newTask = new Task(taskDTO, ong);
		newTask.setId(Long.valueOf(0));
		logger.info("Starting to save task with name={}", newTask.getName());
		try {
			logger.info("Saving task with name={}", newTask.getName());
			Task taskSave = taskRepository.save(newTask);
			logger.info("Saving task with name={}", newTask.getName());
			return new TaskDTO(taskSave);
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
	}
	
	/*
	 * Update task.
	 * @Param String token
	 * @Param Long id
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	
	@Override
	public TaskDTO updateTask(String token, Long id, TaskDTO newTaskDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
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
			throw new UsernameNotFoundException("You cannot update a task which does not belong to your ONG.");
		}
		throw new IllegalArgumentException("This task does not exist!");
		
	}
	
	/*
	 * Delete task.
	 * @Param Long id
	 * @Param String token
	 * @Return TaskDTO
	 */
	
	
	@Override
	public void deleteTask(String token, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(id);
		if (task.isPresent()) {
			if (task.get().getOng().getId() == ong.getId()) {
				taskRepository.delete(task.get());
			} else {
				throw new UsernameNotFoundException("You cannot delete a task which does not belong to your ONG.");
			}

		} else {
			throw new IllegalArgumentException("This task does not exist!");
		}
	}

	/*
	 * Get all attendances from a task.
	 * @Param String token
	 * @Param Long id
	 * @Return List<AttendanceDTO>
	 */
	
	@Override
	public List<AttendanceDTO> getAllAttendancesByTask(String token, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		System.out.println(ong != null);
		List<AttendanceDTO> attendances = new ArrayList<>();
		//Logged as ONG
		if(ong != null) {
			Optional<Task> optionalTask = taskRepository.findById(id);
			// If the task you are searching for is part of your ONG
			if(optionalTask.isPresent() && ong.getTasks().contains(optionalTask.get())) {
				for(Attendance attendance: optionalTask.get().getAttendance()) {
					AttendanceDTO attendanceDTO = new AttendanceDTO(attendance);
					attendances.add(attendanceDTO);
				}
			}else {
				throw new UsernameNotFoundException("You cannot get information about an ONG which you do not belong to.");
			}
		}else {
			throw new UsernameNotFoundException("You must be logged as an ONG to make this operation.");
		}
		return attendances;
	}

	/*
	 * Get all attendances from a volunteer (being a logged ONG or volunteer).
	 * @Param String token
	 * @Param Long id
	 * @Return List<AttendanceDTO>
	 */
	
	@Override
	public List<AttendanceDTO> getAllAttendancesByVolunteer(String token, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong loggedOng = ongRepository.findByUsername(username);
		Volunteer loggedVolunteer = volunteerRepository.findByUsername(username);
		List<AttendanceDTO> attendances = new ArrayList<>();
		
		Optional<Volunteer> volunteer = volunteerRepository.findById(id);
		if(volunteer.isPresent()) {
			//Logged as ONG
			if(loggedOng != null) {
				//If the volunteer you are searching for is part of your ONG, return the list
				if(volunteer.get().getOng().equals(loggedOng)) {
					for(Attendance attendance: volunteer.get().getAttendance()) {
						AttendanceDTO attendanceDTO = new AttendanceDTO(attendance);
						attendances.add(attendanceDTO);
					}
				}else {
					throw new UsernameNotFoundException("You cannot get information about volunteers who do not belong to your ONG.");
				}		
			//Logged as volunteer
			}else if(loggedVolunteer != null) {
				//If the volunteer you are searching for is yourself, return the list
				if(volunteer.get().equals(loggedVolunteer)) {
					for(Attendance attendance: loggedVolunteer.getAttendance()) {
						AttendanceDTO attendanceDTO = new AttendanceDTO(attendance);
						attendances.add(attendanceDTO);
					}
				}else {
					throw new UsernameNotFoundException("As a volunteer, you cannot get information about other volunteers.");
				}
			}
		}else{
			throw new IllegalArgumentException("This volunteer does not exist!");
		}
		
		return attendances;
	}

	/*
	 * Get number of tasks of your own ONG.
	 * @Param String token
	 * @Return Integer
	 */
	
	@Override
	public Integer getNumberOfTasks(String token) {
		Integer count;
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong loggedOng = ongRepository.findByUsername(username);
		if(loggedOng != null) {
			count = loggedOng.getTasks().size();
		}else {
			throw new UsernameNotFoundException("You must be logged as ONG to make this operation.");
		}
		return count;
	}

}
