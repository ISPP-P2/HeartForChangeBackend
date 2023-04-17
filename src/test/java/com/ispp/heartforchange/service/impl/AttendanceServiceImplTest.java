package com.ispp.heartforchange.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.PersonDTO;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.AttendanceType;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AttendanceRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.PersonRepository;
import com.ispp.heartforchange.repository.TaskRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AttendanceServiceImplTest {
	
	@InjectMocks
	private AttendanceServiceImpl attendanceService;

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private OngServiceImpl ongServiceImpl;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	PasswordEncoder encoder;

	@Mock
	ONGRepository ongRepository;
	
	@Mock
	PersonRepository personRepository;
	
	@Mock
	VolunteerRepository volunteerRepository;
	
	@Mock
	AttendanceRepository attendanceRepository;
	
	public Ong createOng() throws OperationNotAllowedException {
		Ong ong = new Ong();
		ong.setCif("G17777777");
		ong.setDescription("description");
		ong.setEmail("test@gmail.com");
		ong.setId(Long.valueOf(0));
		ong.setName("test");
		ong.setPassword("asdf1234");
		ong.setRolAccount(RolAccount.ONG);
		ong.setUsername("test");
		return ong;
	}
	
	public Volunteer createVolunteer() throws OperationNotAllowedException {
        Ong ong = createOng();
        Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());
        PersonDTO personDto = new PersonDTO(person);
        Volunteer volunteer = new Volunteer(personDto, "10:00 y 11:00", false);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
        return volunteer;
    }
	
	public Task createTask(TaskType type) throws OperationNotAllowedException{
		return new Task(Long.valueOf(0), type, "Ruta al Rio", LocalDateTime.of(2024, 04, 15, 23, 03), "Carlos", true, "No procede", "No procede", "Antonio", 20, "Sevilla", new ArrayList<Attendance>(), null);
	}
	
	
	@Test
	public void getAttendanceByIdTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.TALLER);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(attendance));
		
		AttendanceDTO res = attendanceService.getAttendanceById(Long.valueOf(1), token);
		assertNotNull(res);
		
	}
	
	
	@Test
	public void createPetitionTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		
		AttendanceDTO res = attendanceService.createPetition(Long.valueOf(1), token);
		assertNotNull(res);
		
	}
	
	
	@Test
	public void cancelPetitionTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(attendanceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(attendance));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		
		AttendanceDTO res = attendanceService.cancelPetition(Long.valueOf(1), token);
		assertNotNull(res);
		
	}
	
	@Test
	public void acceptPetitionTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(attendanceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(attendance));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		
		AttendanceDTO res = attendanceService.acceptPetition(Long.valueOf(1), token);
		assertNotNull(res);
		
	}
	
	@Test
	public void denyPetitionTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(attendanceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(attendance));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		
		AttendanceDTO res = attendanceService.denyPetition(Long.valueOf(1), token);
		assertNotNull(res);
		
	}
	
	@Test
	public void confirmAttendanceTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(attendanceRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(attendance));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		
		AttendanceDTO res = attendanceService.confirmAttendance(token, AttendanceType.PARCIAL, 1L);
		assertNotNull(res);
		
	}
	
	
	@Test
	public void addBeneficiaryTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.BENEFICIARY);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(personRepository.findById(1L)).thenReturn(Optional.of(volunteer));
		
		AttendanceDTO res = attendanceService.addBeneficiary(1L, token,1L);
		assertNotNull(res);
		
	}
	
	@Test
	public void deleteBeneficiaryTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.BENEFICIARY);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByPersonIdAndTaskId(1L, 1L)).thenReturn(Optional.of(attendance));
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(personRepository.findById(1L)).thenReturn(Optional.of(volunteer));
		
		attendanceService.deleteBeneficiary(1L, token,1L);		
	}
	
	@Test
	public void deleteAttendancesByVolunteerTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.BENEFICIARY);
		Attendance attendance = new Attendance(volunteer, task); 
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(attendanceRepository.findByPersonIdAndTaskId(1L, volunteer.getId())).thenReturn(Optional.of(attendance));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		
		attendanceService.deleteAttendanceByVolunteer(1L, token);
		
	}

	@Test
	public void getAllAttendancesByIdTaskTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.BENEFICIARY);
		Attendance attendance = new Attendance(volunteer, task); 
		task.setAttendance(List.of(attendance));
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.save(any())).thenReturn(attendance);
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);	
		List<AttendanceDTO> res = attendanceService.getAllAttendanceByIdTask(1L, token);
		assertNotNull(res);
		
	}
	

}