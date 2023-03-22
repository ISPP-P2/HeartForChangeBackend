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
			}else throw new UsernameNotFoundException("You dont have the permisions");
		}
	}
	
	/*
	 * Get all task by Ong.
	 * @Param String token
	 * @Param Long id
	 * @Return List<TaskDTO>
	 */

	@Override
    public List<TaskDTO> getByOng(String token) {

        //Identify if user logged is an ong or a volunteer
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Ong loggedOng = ongRepository.findByUsername(username);
        Volunteer loggedVolunteer = volunteerRepository.findByUsername(username);

		Ong ong = loggedOng;

		if(username == null){
			throw new UsernameNotFoundException("You must be logged to make this operation.");
		}

		if(loggedOng == null){
			ong = loggedVolunteer.getOng();
		}

		if(ong == null){
			throw new UsernameNotFoundException("You cannot get information about an ONG which is not yours.");
		}

		List<Task> tasks = taskRepository.findByOng(loggedOng);
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
		for(Task task : tasks) {
			TaskDTO tasktDTO = new TaskDTO(task);
			tasksDTO.add(tasktDTO);
		}
		logger.info("Trying to get the tasks from ONG with name={}", ong.getName());
		return  tasksDTO;
    }
	
	
	
	/*
	 * Get all Activity by Ong.
	 * @Param String token
	 * @Param Long id
	 * @Return List<TaskDTO>
	 */

	@Override
    public List<TaskDTO> getActivityByOng(String token) {

        //Identify if user logged is an ong or a volunteer
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Ong loggedOng = ongRepository.findByUsername(username);
        Volunteer loggedVolunteer = volunteerRepository.findByUsername(username);

		Ong ong = loggedOng;

		if(username == null){
			throw new UsernameNotFoundException("You must be logged to make this operation.");
		}

		if(loggedOng == null){
			ong = loggedVolunteer.getOng();
		}

		if(ong == null){
			throw new UsernameNotFoundException("You cannot get information about an ONG which is not yours.");
		}

		List<Task> tasks = taskRepository.findyByOngAndActivity(loggedOng);
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
		for(Task task : tasks) {
			TaskDTO tasktDTO = new TaskDTO(task);
			tasksDTO.add(tasktDTO);
		}
		logger.info("Trying to get the actividad from ONG with name={}", ong.getName());
		return  tasksDTO;
    }
	
	
	/*
	 * Get all Taller by Ong.
	 * @Param String token
	 * @Param Long id
	 * @Return List<TaskDTO>
	 */

	@Override
    public List<TaskDTO> getTallerByOng(String token) {

        //Identify if user logged is an ong or a volunteer
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Ong loggedOng = ongRepository.findByUsername(username);
        Volunteer loggedVolunteer = volunteerRepository.findByUsername(username);

		Ong ong = loggedOng;

		if(username == null){
			throw new UsernameNotFoundException("You must be logged to make this operation.");
		}

		if(loggedOng == null){
			ong = loggedVolunteer.getOng();
		}

		if(ong == null){
			throw new UsernameNotFoundException("You cannot get information about an ONG which is not yours.");
		}

		List<Task> tasks = taskRepository.findyByOngAndTaller(loggedOng);
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
		for(Task task : tasks) {
			TaskDTO tasktDTO = new TaskDTO(task);
			tasksDTO.add(tasktDTO);
		}
		logger.info("Trying to get the taller from ONG with name={}", ong.getName());
		return  tasksDTO;
    }
	
	
	/*
	 * Get all Curso by Ong.
	 * @Param String token
	 * @Param Long id
	 * @Return List<TaskDTO>
	 */

	@Override
    public List<TaskDTO> getCursoByOng(String token) {

        //Identify if user logged is an ong or a volunteer
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Ong loggedOng = ongRepository.findByUsername(username);
        Volunteer loggedVolunteer = volunteerRepository.findByUsername(username);

		Ong ong = loggedOng;

		if(username == null){
			throw new UsernameNotFoundException("You must be logged to make this operation.");
		}

		if(loggedOng == null){
			ong = loggedVolunteer.getOng();
		}

		if(ong == null){
			throw new UsernameNotFoundException("You cannot get information about an ONG which is not yours.");
		}

		List<Task> tasks = taskRepository.findyByOngAndCurso(loggedOng);
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
		for(Task task : tasks) {
			TaskDTO tasktDTO = new TaskDTO(task);
			tasksDTO.add(tasktDTO);
		}
		logger.info("Trying to get the curso from ONG with name={}", ong.getName());
		return  tasksDTO;
    }
	

	/*
	 * Save Activity.
	 * @Param String token
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	@Override
	public TaskDTO saveActivity(String token, TaskDTO taskDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Task newTask = new Task(taskDTO, ong, TaskType.ACTIVIDAD);
		newTask.setId(Long.valueOf(0));
		logger.info("Starting to save Activity with name={}", newTask.getName());
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
	 * Save Curso.
	 * @Param String token
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	@Override
	public TaskDTO saveCurso(String token, TaskDTO taskDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Task newTask = new Task(taskDTO, ong, TaskType.CURSO);
		newTask.setId(Long.valueOf(0));
		logger.info("Starting to save Activity with name={}", newTask.getName());
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
	 * Save Taller.
	 * @Param String token
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	@Override
	public TaskDTO saveTaller(String token, TaskDTO taskDTO) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Task newTask = new Task(taskDTO, ong, TaskType.TALLER);
		newTask.setId(Long.valueOf(0));
		logger.info("Starting to save Activity with name={}", newTask.getName());
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
	 * Update Activity.
	 * @Param String token
	 * @Param Long id
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	
	@Override
	public TaskDTO updateActivity(String token, Long id, TaskDTO newTaskDTO) {
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
				taskToUpdate.get().setType(TaskType.ACTIVIDAD);
				taskToUpdate.get().setNumParticipants(newTaskDTO.getNumParticipants());
				taskToUpdate.get().setObservations(newTaskDTO.getObservations());
				taskToUpdate.get().setPlace(newTaskDTO.getPlace());
				taskToUpdate.get().setTeacher(newTaskDTO.getTeacher());
				taskToUpdate.get().setOng(ong);
				Task taskSave = taskRepository.save(taskToUpdate.get());
				newTaskDTO.setType(taskSave.getType());
				newTaskDTO.setId(taskSave.getId());
				return newTaskDTO;
			}
			throw new UsernameNotFoundException("You cannot update a task which does not belong to your ONG.");
		}
		throw new IllegalArgumentException("This task does not exist!");
		
	}
	
	
	/*
	 * Update Curso.
	 * @Param String token
	 * @Param Long id
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	
	@Override
	public TaskDTO updateCurso(String token, Long id, TaskDTO newTaskDTO) {
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
				taskToUpdate.get().setType(TaskType.CURSO);
				taskToUpdate.get().setNumParticipants(newTaskDTO.getNumParticipants());
				taskToUpdate.get().setObservations(newTaskDTO.getObservations());
				taskToUpdate.get().setPlace(newTaskDTO.getPlace());
				taskToUpdate.get().setTeacher(newTaskDTO.getTeacher());
				taskToUpdate.get().setOng(ong);
				Task taskSave = taskRepository.save(taskToUpdate.get());
				newTaskDTO.setType(taskSave.getType());
				newTaskDTO.setId(taskSave.getId());
				return newTaskDTO;
			}
			throw new UsernameNotFoundException("You cannot update a task which does not belong to your ONG.");
		}
		throw new IllegalArgumentException("This task does not exist!");
		
	}
	
	
	
	/*
	 * Update Taller.
	 * @Param String token
	 * @Param Long id
	 * @Param TaskDTO taskDTO
	 * @Return TaskDTO
	 */
	
	@Override
	public TaskDTO updateTaller(String token, Long id, TaskDTO newTaskDTO) {
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
				taskToUpdate.get().setType(TaskType.TALLER);
				taskToUpdate.get().setNumParticipants(newTaskDTO.getNumParticipants());
				taskToUpdate.get().setObservations(newTaskDTO.getObservations());
				taskToUpdate.get().setPlace(newTaskDTO.getPlace());
				taskToUpdate.get().setTeacher(newTaskDTO.getTeacher());
				taskToUpdate.get().setOng(ong);
				Task taskSave = taskRepository.save(taskToUpdate.get());
				newTaskDTO.setType(taskSave.getType());
				newTaskDTO.setId(taskSave.getId());
				return newTaskDTO;
			}
			throw new UsernameNotFoundException("You cannot update a task which does not belong to your ONG.");
		}
		throw new IllegalArgumentException("This task does not exist!");
		
	}
	
	
	/*
	 * Delete Activity.
	 * @Param Long id
	 * @Param String token
	 * @Return TaskDTO
	 */
	
	
	@Override
	public void deleteActivity(String token, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(id);
		if (task.isPresent()) {
			if(task.get().getType().equals(TaskType.ACTIVIDAD)) {				
				if (task.get().getOng().getId() == ong.getId()) {
					taskRepository.delete(task.get());
				} else {
					throw new UsernameNotFoundException("You cannot delete a task which does not belong to your ONG.");
				}
			}else throw new IllegalArgumentException("This task is not a Actividad!");

		} else {
			throw new IllegalArgumentException("This task does not exist!");
		}
	}
	
	
	
	/*
	 * Delete Curso.
	 * @Param Long id
	 * @Param String token
	 * @Return TaskDTO
	 */
	
	
	@Override
	public void deleteCurso(String token, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(id);
		if (task.isPresent()) {
			if(task.get().getType().equals(TaskType.CURSO)) {				
				if (task.get().getOng().getId() == ong.getId()) {
					taskRepository.delete(task.get());
				} else {
					throw new UsernameNotFoundException("You cannot delete a task which does not belong to your ONG.");
				}
			}else throw new IllegalArgumentException("This task is not a Curso!");

		} else {
			throw new IllegalArgumentException("This task does not exist!");
		}
	}
	
	
	
	/*
	 * Delete Taller.
	 * @Param Long id
	 * @Param String token
	 * @Return TaskDTO
	 */
	
	
	@Override
	public void deleteTaller(String token, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(id);
		if (task.isPresent()) {
			if(task.get().getType().equals(TaskType.TALLER)) {				
				if (task.get().getOng().getId() == ong.getId()) {
					taskRepository.delete(task.get());
				} else {
					throw new UsernameNotFoundException("You cannot delete a task which does not belong to your ONG.");
				}
			}else throw new IllegalArgumentException("This task is not a Taller!");

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
