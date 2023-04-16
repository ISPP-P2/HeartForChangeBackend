package com.ispp.heartforchange.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.AttendanceType;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.PetitionState;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;
import com.ispp.heartforchange.repository.AttendanceRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.PersonRepository;
import com.ispp.heartforchange.repository.TaskRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.AttendanceService;


@Service
public class AttendanceServiceImpl implements AttendanceService{
	
	private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);

	private AttendanceRepository attendanceRepository;
	private ONGRepository ongRepository;
	private TaskRepository taskRepository;
	private PersonRepository personRepository;
	private JwtUtils jwtUtils;
	
	
	/*
	 * Dependency injection
	 */
	
	public AttendanceServiceImpl(AttendanceRepository attendanceRepository, TaskRepository taskRepository, ONGRepository ongRepository,PersonRepository personRepository, JwtUtils jwtUtils) {
		super();
		this.attendanceRepository = attendanceRepository;
		this.taskRepository = taskRepository;
		this.personRepository = personRepository;
		this.ongRepository = ongRepository;
		this.jwtUtils = jwtUtils;
	}
	
	
	/*
	 * Get attendance by id.
	 * @Param Long id
	 * @Param String token
	 * @Return AttendanceDTO
	 */
	
	@Override
	public AttendanceDTO getAttendanceById(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Person person = personRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);

		if (attendance == null) {
			throw new UsernameNotFoundException("Attendace not found!");
		} else {
			if (ong != null && attendance.get().getTask().getOng().getId() == ong.getId()) {
				return new AttendanceDTO(attendance.get());
			} else if(person != null && attendance.get().getPerson().getId() == person.getId()){
				return new AttendanceDTO(attendance.get());
			}else throw new UsernameNotFoundException("You don't have access!");
		}
		
	}
	
	/*
	 * Create Petition for attendance by a Volunteer.
	 * @Param Long id
	 * @Param String token
	 * @Return AttendanceDTO
	 */
	
	@Override
	public AttendanceDTO createPetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Person person = personRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(id);
		if (task.isPresent()) { 
			// Checks if the task is an Activity 
			if(task.get().getType() == TaskType.ACTIVIDAD) {
				//Checks if the task belongs to the right ONG
				if(task.get().getOng().equals(person.getOng())) {
					//Checks if the petition is not being created after the tasks date
					if(!task.get().getDate().isBefore(LocalDateTime.now())) {
						
						Attendance attendance = new Attendance(person, task.get());
						attendance.setId(Long.valueOf(0));
						try {
							Attendance attendanceSaved = attendanceRepository.save(attendance);
							logger.info("Create Petition on Task with name={} by User with username={}", task.get().getName(), person.getName());
							AttendanceDTO attendanceDTO = new AttendanceDTO(attendanceSaved);
							return attendanceDTO;
						} catch (Exception e) {
							throw new UsernameNotFoundException(e.getMessage());
						}
					}else throw new UsernameNotFoundException("You cant create a petition on a task thats is before today!");
					
				}else throw new UsernameNotFoundException("You cant create a petition on this task because it doesn belong to your ONG!");
				
			}else throw new UsernameNotFoundException("You cant create a petition on this task because its not an Activity!");
		}
		throw new UsernameNotFoundException("Task doesn't exist!");
		
	} 
	
	
	
	/*
	 * Cancel a Petition for attendance by a Volunteer.
	 * @Param Long id
	 * @Param String token
	 */
	
	@Override
	public AttendanceDTO cancelPetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Person person = personRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);
		if (attendance.isPresent()) {
			if (attendance.get().getPerson().getId() == person.getId()) {
				try {					
					attendance.get().setState(PetitionState.CANCELADA);
					AttendanceDTO attendanceDTO = new AttendanceDTO(attendance.get());
					return attendanceDTO;
				} catch (Exception e) {
					throw new UsernameNotFoundException(e.getMessage());
				}
			} else {
				throw new UsernameNotFoundException("You dont have the permisions on this attendance!");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	
	/*
	 * Accept a Petition for attendance by a ONG.
	 * @Param Long id
	 * @Param String token
	 * @Return AttendanceDTO
	 */
	
	@Override
	public AttendanceDTO acceptPetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);
		if (attendance.isPresent()) {
			if (attendance.get().getTask().getOng().getId() == ong.getId()) {
				//Checks if the petition is not being accepted after the tasks date
				if(!attendance.get().getTask().getDate().isBefore(LocalDateTime.now())) {					
					logger.info("Attendance with id={} is being accepting ", attendance.get().getId());
					attendance.get().setState(PetitionState.ACEPTADA);
					Attendance attendanceUpdate = attendance.get();
					System.out.println(attendanceUpdate);
					try {
						attendanceRepository.save(attendanceUpdate);
						return new AttendanceDTO(attendanceUpdate);
					} catch (Exception e) {
						throw new UsernameNotFoundException(e.getMessage());
					}
				}else throw new UsernameNotFoundException("You cant accept an attendace wich tasks date is before now!");
			} else {
				throw new UsernameNotFoundException("You dont have permision on an attendance that doesnt belong to your ONG!");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	/*
	 * Deny a Petition for attendance by a ONG.
	 * @Param Long id
	 * @Param String token
	 * @Return AttendanceDTO
	 */
	
	@Override
	public AttendanceDTO denyPetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);
		if (attendance.isPresent()) {
			if (attendance.get().getTask().getOng().getId() == ong.getId()) {
				//Checks if the petition is not being accepted after the tasks date
				if(!attendance.get().getTask().getDate().isBefore(LocalDateTime.now())) {					
					logger.info("Attendance with id={} is being denying ", attendance.get().getId());
					attendance.get().setState(PetitionState.DENEGADA);
					Attendance attendanceUpdate = attendance.get();
					System.out.println(attendanceUpdate);
					try {
						attendanceRepository.save(attendanceUpdate);
						return new AttendanceDTO(attendanceUpdate);
					} catch (Exception e) {
						throw new UsernameNotFoundException(e.getMessage());
					}
				}throw new UsernameNotFoundException("You cant deny an attendace wich tasks date is before now!");
			} else {
				throw new UsernameNotFoundException("You dont have the permisions");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	/*
	 * Confirm attendance by a ONG.
	 * @Param Long id
	 * @Param AttendanceType type
	 * @Param String token
	 * @Return AttendanceDTO
	 */
	
	
	@Override
	public AttendanceDTO confirmAttendance(String token, AttendanceType type, Long id) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);
		if (attendance.isPresent()) {
			if (attendance.get().getTask().getOng().getId() == ong.getId()) {
				logger.info("Attendance with id={} is being confirm", attendance.get().getId());
				attendance.get().setType(type);
				Attendance attendanceUpdate = attendance.get();
				System.out.println(attendanceUpdate);
				try {
					attendanceRepository.save(attendanceUpdate);
					return new AttendanceDTO(attendanceUpdate);
				} catch (Exception e) {
					throw new UsernameNotFoundException(e.getMessage());
				}
			} else {
				throw new UsernameNotFoundException("You dont have the permisions!");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	/*
	 * Add beneficiary for attendance by a ONG.
	 * @Param Long idTask
	 * @Param Long idPerson
	 * @Param String token
	 * @Return AttendanceDTO
	 */
	
	
	@Override
	public AttendanceDTO addBeneficiary(Long idTask, String token, Long idPerson) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Person> person = personRepository.findById(idPerson);
		Optional<Task> task = taskRepository.findById(idTask);
		if (task.isPresent() && person.isPresent()) {
			if(task.get().getType() == TaskType.CURSO || task.get().getType() == TaskType.TALLER) {
				if(task.get().getOng().getId() == ong.getId() && person.get().getOng().equals(ong)) {
					
					Attendance attendance = new Attendance(person.get(), task.get(), PetitionState.ACEPTADA);
					attendance.setId(Long.valueOf(0));
					logger.info("Create Petition on Task with name={} by User with username={}", task.get().getName());
					try {
						Attendance attendanceSaved = attendanceRepository.save(attendance); 
						return new AttendanceDTO(attendanceSaved);
					} catch (Exception e) {
						throw new UsernameNotFoundException(e.getMessage());
					}
				}
				throw new UsernameNotFoundException("You dont have the permissions!");
			}
			throw new UsernameNotFoundException("This task isnt a CURSO or a TALLER!");
		}
		throw new UsernameNotFoundException("Attendance or Person doesn't exist!");
		
	}
	
	/*
	 * Delete beneficiary for attendance by a ONG.
	 * @Param Long idTask
	 * @Param Long idPerson
	 * @Param String token
	 */
	public void deleteAttendanceByVolunteer(Long idTask, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Person person = personRepository.findByUsername(username);

		Optional<Attendance> attendance = attendanceRepository.findByPersonIdAndTaskId(idTask, person.getId());
		if(attendance.isEmpty()){
			throw new UsernameNotFoundException("This attendance not exist!");
		}
		if (attendance.get().getTask().getOng().getId() != person.getOng().getId()) {
			throw new UsernameNotFoundException("Error deleting attendance");
		}
		attendanceRepository.delete(attendance.get());

	}
	@Override
	public void deleteBeneficiary(Long idTask, String token, Long idPerson) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findByPersonIdAndTaskId(idTask, idPerson);
		if (attendance.isPresent()) {
			if (attendance.get().getTask().getOng().getId() == ong.getId()) {
				System.out.println(attendance);
				attendanceRepository.delete(attendance.get());
			} else {
				throw new UsernameNotFoundException("Error deleting attendance");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}

	/*
	 * Get all attendances by id task
	 * @Param Long idTask
	 * @Param String token
	 * @Return List<AttendanceDTO>
	 */

	public List<AttendanceDTO> getAllAttendanceByIdTask(Long idTask, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(idTask);

		List<AttendanceDTO> attendancesDTO = new ArrayList<AttendanceDTO>();
		if(task.isPresent()) {
			if(ong==null) {
				throw new UsernameNotFoundException("You must be an ONG to use this method!");
			}
			if(!task.get().getOng().equals(ong)){
				throw new UsernameNotFoundException("You cannot get information about other ONGs!");
			}
			for(Attendance att: task.get().getAttendance()) {
				AttendanceDTO attDTO = new AttendanceDTO(att);
				attendancesDTO.add(attDTO);
			}
		}else {
			throw new UsernameNotFoundException("Task not found!");
		}
		return attendancesDTO;
	}
	
	
	
	
	
}