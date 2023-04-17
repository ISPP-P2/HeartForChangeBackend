package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.entity.Attendance;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CourseControllerTest {
	
	@Mock
	private TaskServiceImpl taskService;

	@Mock
	private JwtUtils jwtUtils;
	
	@InjectMocks
	private CourseController courseController;
	
	
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
	
	
	public Task createTask(TaskType type) throws OperationNotAllowedException{
		return new Task(Long.valueOf(0), type, "Ruta al Rio", LocalDateTime.of(2023, 04, 15, 23, 03), "Carlos", true, "No procede", "No procede", "Antonio", 20, "Sevilla", new ArrayList<Attendance>(), null);
	}
	
	
	@Test
	public void createCourseTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.saveCurso("oken", taskDTO)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = courseController.save(request, taskDTO);
		assertEquals(res, ResponseEntity.ok(taskDTO));
	}
	
	@Test
	public void getTaskByIdTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		when(taskService.getById("oken", 1L)).thenReturn(taskDTO);
		
		ResponseEntity<?> res = courseController.getTaskById(request, 1L);
		assertEquals(res, ResponseEntity.ok(taskDTO));
	}
	
	@Test
	public void getTaskByIdNegativaTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		
		when(taskService.getById("oken", 1L)).thenReturn(taskDTO);
		
		ResponseEntity<?> res = courseController.getTaskById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getByOngTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.getCursoByOng("oken")).thenReturn(lista);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = courseController.getByOng(request);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getByOngNegativeTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.getCursoByOng("oken")).thenReturn(lista);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = courseController.getByOng(request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void updateTest() throws OperationNotAllowedException {

		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.updateCurso("oken", 1L, taskDTO)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = courseController.update(request, taskDTO, 1L);
		assertEquals(res, ResponseEntity.ok(taskDTO));
	}
	
	@Test
	public void updateNegativeTest() throws OperationNotAllowedException {

		Ong ong = createOng();
		Task task = createTask(TaskType.CURSO);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.updateCurso("oken", 1L, taskDTO)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = courseController.update(request, taskDTO, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteTest() throws OperationNotAllowedException {


		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = courseController.deleteWorkshop(1L, request);
		assertEquals(res, ResponseEntity.ok("Course deleted"));
	}
	
	@Test
	public void deleteNegativeTest() throws OperationNotAllowedException {


		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = courseController.deleteWorkshop(1L, request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getBeneficiariesByIdTest() throws OperationNotAllowedException {
		
		List<BeneficiaryDTO> lista = new ArrayList<BeneficiaryDTO>();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getAllBeneficiariesByTask("oken", 1L)).thenReturn(lista);
		
		ResponseEntity<?> res = courseController.getBeneficiariesById(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getBeneficiariesByIdNegativeTest() throws OperationNotAllowedException {
		
		List<BeneficiaryDTO> lista = new ArrayList<BeneficiaryDTO>();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getAllBeneficiariesByTask("oken", 1L)).thenReturn(lista);
		
		ResponseEntity<?> res = courseController.getBeneficiariesById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getAttendancesByIdTest() throws OperationNotAllowedException {
		
		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getAllAttendancesByCourse("oken", 1L) ).thenReturn(lista);
		
		ResponseEntity<?> res = courseController.getAttendancesById(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendancesByIdNegativeTest() throws OperationNotAllowedException {
		
		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getAllAttendancesByCourse("oken", 1L) ).thenReturn(lista);
		
		ResponseEntity<?> res = courseController.getBeneficiariesById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getStatePetitionByOngTest() throws OperationNotAllowedException {
		
		AttendanceDTO attendance = new AttendanceDTO();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getPetitionStateByONG("oken", 1L, 1L) ).thenReturn(attendance);
		
		ResponseEntity<?> res = courseController.getStatePetitionByONG(request, 1L, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void getStatePetitionByOngNegativeTest() throws OperationNotAllowedException {
		
		AttendanceDTO attendance = new AttendanceDTO();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getPetitionStateByONG("oken", 1L, 1L) ).thenReturn(attendance);
		
		ResponseEntity<?> res = courseController.getStatePetitionByONG(request, 1L, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void getAttendancesByPersonIdTest() throws OperationNotAllowedException {
		
		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getAllAttendancesByBeneficiary("oken", 1L) ).thenReturn(lista);
		
		ResponseEntity<?> res = courseController.getAttendancesByPersonId(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendancesByPersonIdNegativeTest() throws OperationNotAllowedException {
		
		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		when(taskService.getAllAttendancesByBeneficiary("oken", 1L) ).thenReturn(lista);
		
		ResponseEntity<?> res = courseController.getAttendancesByPersonId(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
}
