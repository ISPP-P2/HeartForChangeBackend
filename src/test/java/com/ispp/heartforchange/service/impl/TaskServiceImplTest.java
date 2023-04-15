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
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.PersonDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.Beneficiary;
import com.ispp.heartforchange.entity.CivilStatus;
import com.ispp.heartforchange.entity.DocumentType;
import com.ispp.heartforchange.entity.Gender;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.Person;
import com.ispp.heartforchange.entity.PetitionState;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;
import com.ispp.heartforchange.entity.Volunteer;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.repository.AttendanceRepository;
import com.ispp.heartforchange.repository.BeneficiaryRepository;
import com.ispp.heartforchange.repository.ONGRepository;
import com.ispp.heartforchange.repository.PersonRepository;
import com.ispp.heartforchange.repository.TaskRepository;
import com.ispp.heartforchange.repository.VolunteerRepository;
import com.ispp.heartforchange.security.jwt.JwtUtils;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskServiceImplTest {
	
	@InjectMocks
	private TaskServiceImpl taskService;

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
	BeneficiaryRepository beneficiaryRepository;
	
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
		return new Task(Long.valueOf(0), type, "Ruta al Rio", LocalDateTime.of(2023, 04, 15, 23, 03), "Carlos", true, "No procede", "No procede", "Antonio", 20, "Sevilla", new ArrayList<Attendance>(), null);
	}
	
	@Test
	public void createActivityTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		TaskDTO  taskDTO = new TaskDTO(task);
		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.save(any())).thenReturn(task);

		TaskDTO taskSaved = taskService.saveActivity(token, taskDTO);
		assertNotNull(taskSaved);
	}
	
	@Test
	public void createCourseTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		TaskDTO  taskDTO = new TaskDTO(task);
		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.save(any())).thenReturn(task);

		TaskDTO taskSaved = taskService.saveCurso(token, taskDTO);
		assertNotNull(taskSaved);
	}
	
	@Test
	public void CreateWorkshopTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.TALLER);
		task.setOng(ong);
		TaskDTO  taskDTO = new TaskDTO(task);
		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.save(any())).thenReturn(task);

		TaskDTO taskSaved = taskService.saveTaller(token, taskDTO);
		assertNotNull(taskSaved);
	}
	
	@Test
	public void updateActivityTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Task newTask = task;
		newTask.setName("Update Task");
		TaskDTO  taskDTO = new TaskDTO(task);
		taskDTO.setName("Update Task");
		
		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(taskRepository.save(any())).thenReturn(newTask);

		TaskDTO taskUpdate = taskService.updateActivity(token, Long.valueOf(1), taskDTO);
		assertNotNull(taskUpdate);
	}
	
	@Test
	public void updateCourseTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Task newTask = task;
		newTask.setName("Update Task");
		TaskDTO  taskDTO = new TaskDTO(task);
		taskDTO.setName("Update Task");
		
		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(taskRepository.save(any())).thenReturn(newTask);

		TaskDTO taskUpdate = taskService.updateCurso(token, Long.valueOf(1), taskDTO);
		assertNotNull(taskUpdate);
	}
	
	@Test
	public void updateWorkshopTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.TALLER);
		task.setOng(ong);
		Task newTask = task;
		newTask.setName("Update Task");
		TaskDTO  taskDTO = new TaskDTO(task);
		taskDTO.setName("Update Task");
		
		String token = "tokenprueba";

		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(taskRepository.save(any())).thenReturn(newTask);

		TaskDTO taskUpdate = taskService.updateTaller(token, Long.valueOf(1), taskDTO);
		assertNotNull(taskUpdate);
	}
	
	@Test
	public void getByIdByONGTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.TALLER);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(personRepository.findByUsername("test")).thenReturn(null);
		
		TaskDTO taskDTO = taskService.getById(token, Long.valueOf(1));
		assertNotNull(taskDTO);
		
	}
	
	@Test
	public void getByIdByPersonTest() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		Ong ong = createOng();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(null);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		
		TaskDTO taskDTO = taskService.getById(token, Long.valueOf(1));
		assertNotNull(taskDTO);
		
	}
	
	@Test
	public void getByONGByPersonTest() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		Ong ong = createOng();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(null);
		when(volunteerRepository.findByUsername("test")).thenReturn(volunteer);
		
		List<TaskDTO> taskDTO = taskService.getByOng(token);
		assertNotNull(taskDTO);
		
	}
	
	
	@Test
	public void getByONGByONGTest() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		Ong ong = createOng();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findByUsername("test")).thenReturn(null);
		
		List<TaskDTO> taskDTO = taskService.getByOng(token);
		assertNotNull(taskDTO);
		
	}
	

	@Test
	public void getActivityByONGByONGTest() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		Ong ong = createOng();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findByUsername("test")).thenReturn(null);
		
		List<TaskDTO> taskDTO = taskService.getActivityByOng(token);
		assertNotNull(taskDTO);
		
	}
	
	
	@Test
	public void getActivityByONGByVolunteerTest() throws OperationNotAllowedException {
		Volunteer volunteer = createVolunteer();
		Ong ong = createOng();
		volunteer.setOng(ong);
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(null);
		when(volunteerRepository.findByUsername("test")).thenReturn(volunteer);
		
		List<TaskDTO> taskDTO = taskService.getActivityByOng(token);
		assertNotNull(taskDTO);
		
	}
	
	
	@Test
	public void getWorkshopByONGTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.TALLER);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findByUsername("test")).thenReturn(null);
		
		List<TaskDTO> taskDTO = taskService.getTallerByOng(token);
		assertNotNull(taskDTO);
		
	}
	
	@Test
	public void getCourseByONGTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findByUsername("test")).thenReturn(null);
		
		List<TaskDTO> taskDTO = taskService.getCursoByOng(token);
		assertNotNull(taskDTO);		
	}
	
	
	@Test
	public void deleteActivityTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		taskService.deleteActivity(token, Long.valueOf(1));
	}
	
	@Test
	public void deleteCourseTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		taskService.deleteCurso(token, Long.valueOf(1));
	}
	
	@Test
	public void deleteWorkshopTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.TALLER);
		task.setOng(ong);
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		taskService.deleteTaller(token, Long.valueOf(1));
	}
	
	
	@Test
	public void getAllVoluntariesByTaskAcceptedTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByTaskIdAndState(Long.valueOf(1), PetitionState.ACEPTADA)).thenReturn(new ArrayList<Attendance>());
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<VolunteerDTO> res = taskService.getAllVoluntariesByTaskAccepted(token, Long.valueOf(1));
		assertNotNull(res);		
	}
	
	@Test
	public void getAllBeneficiariesByTaskTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByTaskIdAndState(Long.valueOf(1), PetitionState.ACEPTADA)).thenReturn(new ArrayList<Attendance>());
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<BeneficiaryDTO> res = taskService.getAllBeneficiariesByTask(token, Long.valueOf(1));
		assertNotNull(res);		
	}
	
	
	@Test
	public void getAllVoluntariesByTaskTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByTaskIdAndState(Long.valueOf(1), PetitionState.ACEPTADA)).thenReturn(new ArrayList<Attendance>());
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<VolunteerDTO> res = taskService.getAllVoluntariesByTask(token, Long.valueOf(1));
		assertNotNull(res);		
	}
	
	@Test
	public void getAllAttendancesOngByVolunteerTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		volunteer.setAttendance(new ArrayList<Attendance>());
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(volunteer));
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<AttendanceDTO> res = taskService.getAllAttendancesOngByVolunteer(token, Long.valueOf(1));
		assertNotNull(res);	
	}
	
	@Test
	public void getAllAttendancesVolunteerByVolunteerTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		volunteer.setAttendance(new ArrayList<Attendance>());
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findByUsername("test")).thenReturn(volunteer);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<TaskDTO> res = taskService.getAllAttendancesVolunteerByVolunteer(token);
		assertNotNull(res);	
	}
	
	
	@Test
	public void getNumberOfTaskTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);
		ong.setTasks(List.of(task));
		
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		Integer res = taskService.getNumberOfTasks(token);
		assertNotNull(res);	
	}
	
	@Test
	public void getAllAttendancesByBeneficiaryTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Account account = new Account(ong.getId(), ong.getEmail(), ong.getUsername(), ong.getPassword(), ong.getRolAccount());
		Person person = new Person(Long.valueOf(0), LocalDate.of(2010, 03, 12), "Garcia", "Rodriguez", "Mario", DocumentType.DNI, "78675456P", Gender.MALE, LocalDate.of(2001, 03, 12), CivilStatus.DIVORCED, 0, "Prado", "12893", "Calera", "Sevilla", "698745670", LocalDate.of(2012, 03, 12), "B", "Ninguna", ong, new ArrayList<Attendance>());	    
		PersonDTO personDto = new PersonDTO(person);
		Beneficiary beneficiary = new Beneficiary(personDto, "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		BeneficiaryDTO beneficiaryDto = new BeneficiaryDTO(person, Long.valueOf(0), "España", true, LocalDate.of(2001, 1, 1),true , true,LocalDate.of(2001, 1, 1) , true, "Trabajador", "Hola", true, true, true, true, "Prueba", "Prueba");
		beneficiary.setOng(ong);
		beneficiary.setAttendance(new ArrayList<Attendance>());
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<AttendanceDTO> res = taskService.getAllAttendancesByBeneficiary(token, Long.valueOf(1));
		assertNotNull(res);	
	}
	
	@Test
	public void getAllActivityByOngNoFinishByOngTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		volunteer.setAttendance(new ArrayList<Attendance>());
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(volunteerRepository.findByUsername("test")).thenReturn(null);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<TaskDTO> res = taskService.getActivityByOngNotFinish(token);
		assertNotNull(res);	
	}
	
	@Test
	public void getAllActivityByOngNoFinishByVolunteerTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		volunteer.setAttendance(new ArrayList<Attendance>());
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(null);
		when(volunteerRepository.findByUsername("test")).thenReturn(volunteer);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<TaskDTO> res = taskService.getActivityByOngNotFinish(token);
		assertNotNull(res);	
	}
	
	
	@Test
	public void getPetitionByTaskTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByTaskId(1L)).thenReturn(new ArrayList<Attendance>());
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<AttendanceDTO> res = taskService.getPetitionsByTask(token, 1L);
		assertNotNull(res);	
	}
	
	@Test
	public void getAttendancesByActivityTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);

		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByTaskId(1L)).thenReturn(new ArrayList<Attendance>());
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<AttendanceDTO> res = taskService.getAttendancesByActivity(token, 1L);
		assertNotNull(res);	
	}
	
	@Test
	public void getAttendancesByWorkshopTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.TALLER);
		task.setOng(ong);

		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByTaskId(1L)).thenReturn(new ArrayList<Attendance>());
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<AttendanceDTO> res = taskService.getAllAttendancesByWorkshop(token, 1L);
		assertNotNull(res);	
	}
	
	
	@Test
	public void getAttendancesByCourseTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);

		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(attendanceRepository.findByTaskId(1L)).thenReturn(new ArrayList<Attendance>());
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		List<AttendanceDTO> res = taskService.getAllAttendancesByCourse(token, 1L);
		assertNotNull(res);	
	}
	
	
	@Test
	public void getPetitionStateByVolunteerTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		volunteer.setAttendance(new ArrayList<Attendance>());
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(attendanceRepository.findByPersonIdAndTaskId(1L, 1L)).thenReturn(null);
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		AttendanceDTO res = taskService.getPetitionStateByVolunteer(token, 1L);
		assertNotNull(res);	
	}
	
	@Test
	public void getPetitionStateByOngTest() throws OperationNotAllowedException {
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);
		Volunteer volunteer = createVolunteer();
		volunteer.setOng(ong);
		volunteer.setRolAccount(RolAccount.VOLUNTEER);
		volunteer.setAttendance(new ArrayList<Attendance>());
		
		Attendance attendance = new Attendance(volunteer, task);
		
		ong.setTasks(List.of(task));
		String token = "tokenprueba";
		
		when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn("test");
		when(ongRepository.findByUsername("test")).thenReturn(ong);
		when(personRepository.findByUsername("test")).thenReturn(volunteer);
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		when(attendanceRepository.findByPersonIdAndTaskId(1L, 1L)).thenReturn(Optional.of(attendance));
		when(taskRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(task));
		
		AttendanceDTO res = taskService.getPetitionStateByONG(token, 1L, 1L);
		assertNotNull(res);	
	}
	
}
