package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.PersonDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.entity.Attendance;
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
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.TaskServiceImpl;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskControllerTest {

	
	@Mock
	private TaskServiceImpl taskService;

	@Mock
	private JwtUtils jwtUtils;

	@InjectMocks
	private TaskController taskController;
	
	
	
	
	@Test
	public void getByOngTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.getByOng("oken")).thenReturn(lista);

		ResponseEntity<?> res = taskController.getByOng(request);

		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	
	@Test
	public void getByOngNegativeTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.getByOng("oken")).thenReturn(lista);

		ResponseEntity<?> res = taskController.getByOng(request);

		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void getAttendancesByPersonIdTest() throws OperationNotAllowedException {
		
		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.getAllAttendancesByBeneficiary("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = taskController.getAttendancesByPersonId(request, 1L);

		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendancesByPersonIdNegativeTest() throws OperationNotAllowedException {
		
		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.getAllAttendancesByBeneficiary("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = taskController.getAttendancesByPersonId(request, 1L);

		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getCountTest() throws OperationNotAllowedException {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.getNumberOfTasks("oken")).thenReturn(10);

		ResponseEntity<?> res = taskController.getNumberOfTasks(request);

		assertEquals(res, ResponseEntity.ok(10));
	}
	
	@Test
	public void getCountNegativeTest() throws OperationNotAllowedException {
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.getNumberOfTasks("oken")).thenReturn(10);

		ResponseEntity<?> res = taskController.getNumberOfTasks(request);

		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	
	
	
	

	

	

	
	
	
	
}
