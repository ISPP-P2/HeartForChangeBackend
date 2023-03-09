package com.ispp.heartforchange.service.impl;

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
	
	public AttendanceServiceImpl(AttendanceRepository attendanceRepository, TaskRepository taskRepository, ONGRepository ongRepository,PersonRepository personRepository, JwtUtils jwtUtils) {
		super();
		this.attendanceRepository = attendanceRepository;
		this.taskRepository = taskRepository;
		this.personRepository = personRepository;
		this.ongRepository = ongRepository;
		this.jwtUtils = jwtUtils;
	}
	
	@Override
	public List<AttendanceDTO> getAll() {
		List<Attendance> attendances = attendanceRepository.findAll();
		List<AttendanceDTO> attendancesDTO = new ArrayList<AttendanceDTO>();
		for(Attendance attendance : attendances) {
			AttendanceDTO attendanceDTO = new AttendanceDTO(attendance);
					attendancesDTO.add(attendanceDTO);
		}
		return attendancesDTO;
	}
	
	@Override
	public AttendanceDTO getAttendanceById(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Optional<Ong> ong = Optional.of(ongRepository.findByUsername(username));
		Optional<Person> person = Optional.of(personRepository.findByUsername(username));
		Optional<Attendance> attendance = attendanceRepository.findById(id);

		if (!attendance.isPresent()) {
			throw new UsernameNotFoundException("Attendace not found!");
		} else {
			if (ong.isPresent() && attendance.get().getTask().getOng().getId() == ong.get().getId()) {
				return new AttendanceDTO(attendance.get());
			} else if(person.isPresent() && attendance.get().getPerson().getId() == person.get().getId()){
				return new AttendanceDTO(attendance.get());
			}else throw new UsernameNotFoundException("You don't have access!");
		}
	}
	
	@Override
	public AttendanceDTO getAttendanceByIdVolunteer(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Person person = personRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);

		if (!attendance.isPresent()) {
			throw new UsernameNotFoundException("Attendace not found!");
		} else {
			if (attendance.get().getPerson().getId() == person.getId()) {
				return new AttendanceDTO(attendance.get());
			} else {
				throw new UsernameNotFoundException("You don't have access!");
			}
		}
	}
	
	
	@Override
	public AttendanceDTO createPetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Person person = personRepository.findByUsername(username);
		Optional<Task> task = taskRepository.findById(id);
		if (task.isPresent()) {
			System.out.println(person);
			Attendance attendance = new Attendance(person, task.get());
			attendance.setId(Long.valueOf(0));
			System.out.println(attendance);
			try {
				Attendance attendanceSaved = attendanceRepository.save(attendance);
				logger.info("Create Petition on Task with name={} by User with username={}", task.get().getName(), person.getName());
				AttendanceDTO attendanceDTO = new AttendanceDTO(attendanceSaved);
				return attendanceDTO;
			} catch (Exception e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
		}
		throw new UsernameNotFoundException("Attendance doesn't exist!");
		
	}
	
	
	@Override
	public void deletePetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Person person = personRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);
		if (attendance.isPresent()) {
			if (attendance.get().getPerson().getId() == person.getId()) {
				System.out.println(attendance);
				attendanceRepository.delete(attendance.get());
			} else {
				throw new UsernameNotFoundException("Error deleting attendance");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	
	@Override
	public AttendanceDTO acceptPetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);
		if (attendance.isPresent()) {
			if (attendance.get().getTask().getOng().getId() == ong.getId()) {
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
			} else {
				throw new UsernameNotFoundException("Error accepting this attendance");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	
	@Override
	public AttendanceDTO denyPetition(Long id, String token) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Attendance> attendance = attendanceRepository.findById(id);
		if (attendance.isPresent()) {
			if (attendance.get().getTask().getOng().getId() == ong.getId()) {
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
			} else {
				throw new UsernameNotFoundException("Error denying this attendance");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	
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
				throw new UsernameNotFoundException("Error confirming this attendance");
			}

		} else {
			throw new UsernameNotFoundException("This attendance not exist!");
		}
	}
	
	
	
	@Override
	public AttendanceDTO addBeneficiary(Long idTask, String token, Long idPerson) {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		Ong ong = ongRepository.findByUsername(username);
		Optional<Person> person = personRepository.findById(idPerson);
		Optional<Task> task = taskRepository.findById(idTask);
		if (task.isPresent() && person.isPresent() && task.get().getType() == TaskType.CURSO || task.get().getType() == TaskType.TALLER) {
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
		throw new UsernameNotFoundException("Attendance or Person doesn't exist!");
		
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
	
	
	
	
	
}
