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
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.dto.VolunteerDTO;
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
public class ActivityControllerTest {

	
	@Mock
	private TaskServiceImpl taskService;

	@Mock
	private JwtUtils jwtUtils;
	
	@InjectMocks
	private ActivityController activityController;
	
	
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
	public void createActivityTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.saveActivity("oken", taskDTO)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.save(request, taskDTO);
		assertEquals(res, ResponseEntity.ok(taskDTO));
	}
	
	@Test
	public void createActivityNegativeTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.saveActivity("oken", taskDTO)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.save(request, taskDTO);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getByIdTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.getById("oken", 1L)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.getTaskById(request, 1L);
		assertEquals(res, ResponseEntity.ok(taskDTO));
	}
	
	@Test
	public void getByIdNegativeTest() throws OperationNotAllowedException {
		
		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.getById("oken", 1L)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.getTaskById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getByOngTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.getActivityByOng("oken")).thenReturn(lista);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.getByOng(request);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getByOngNegativeTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.getActivityByOng("oken")).thenReturn(lista);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.getByOng(request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getByOngNotFinishTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.getActivityByOngNotFinish("oken")).thenReturn(lista);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.getByOngNotFinish(request);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getByOngNotFinishNegativeTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.getActivityByOngNotFinish("oken")).thenReturn(lista);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.getByOngNotFinish(request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void updateActivityTest() throws OperationNotAllowedException {

		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(taskService.updateActivity("oken", 1L, taskDTO)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.update(request, taskDTO, 1L);
		assertEquals(res, ResponseEntity.ok(taskDTO));
	}
	
	@Test
	public void updateActivityNegativeTest() throws OperationNotAllowedException {

		Ong ong = createOng();
		Task task = createTask(TaskType.ACTIVIDAD);
		task.setOng(ong);		
		TaskDTO  taskDTO = new TaskDTO(task);
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(taskService.updateActivity("oken", 1L, taskDTO)).thenReturn(taskDTO);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.update(request, taskDTO, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteActivityTest() throws OperationNotAllowedException {


		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.deleteGrant(1L, request);
		assertEquals(res, ResponseEntity.ok("Task deleted"));
	}
	
	@Test
	public void deleteActivityNegativeTest() throws OperationNotAllowedException {


		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = activityController.deleteGrant(1L, request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getAttendacesByIdAcceptedTest() throws OperationNotAllowedException {

		List<VolunteerDTO> lista = new ArrayList<VolunteerDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllVoluntariesByTaskAccepted("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesByIdAccepted(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendacesByIdAcceptedNegativeTest() throws OperationNotAllowedException {

		List<VolunteerDTO> lista = new ArrayList<VolunteerDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllVoluntariesByTaskAccepted("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesByIdAccepted(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getAttendacesByIdTest() throws OperationNotAllowedException {

		List<VolunteerDTO> lista = new ArrayList<VolunteerDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllVoluntariesByTask("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesById(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendacesByIdNegativeTest() throws OperationNotAllowedException {

		List<VolunteerDTO> lista = new ArrayList<VolunteerDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllVoluntariesByTask("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getAttendacesByPersonIdTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllAttendancesVolunteerByVolunteer("oken")).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesByPersonId(request);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendacesByPersonIdNegativeTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllAttendancesVolunteerByVolunteer("oken")).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesByPersonId(request);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getAttendacesByPersonIdOng2Test() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllAttendancesVolunteerByVolunteer("oken")).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesByPersonId(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendacesByPersonIdOng2NegativeTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllAttendancesVolunteerByVolunteer("oken")).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesByPersonId(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getAttendacesByPersonIdOngTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllAttendancesVolunteerByVolunteer("oken")).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesById(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendacesByPersonIdOngNegativeTest() throws OperationNotAllowedException {

		List<TaskDTO> lista = new ArrayList<TaskDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAllAttendancesVolunteerByVolunteer("oken")).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getPetitionsByIdTest() throws OperationNotAllowedException {

		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getPetitionsByTask("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getPetitionsById(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getPetitionsByIdNegativeTest() throws OperationNotAllowedException {

		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getPetitionsByTask("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getPetitionsById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void getAttendancesByTaskTest() throws OperationNotAllowedException {

		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAttendancesByActivity("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getAttendancesByTask(request, 1L);
		assertEquals(res, ResponseEntity.ok(lista));
	}
	
	@Test
	public void getAttendancesByTaskNegativeTest() throws OperationNotAllowedException {

		List<AttendanceDTO> lista = new ArrayList<AttendanceDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getAttendancesByActivity("oken", 1L)).thenReturn(lista);

		ResponseEntity<?> res = activityController.getPetitionsById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void getStatePetitionByVolunteerTest() throws OperationNotAllowedException {

		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getPetitionStateByVolunteer("oken", 1L)).thenReturn(attendance);

		ResponseEntity<?> res = activityController.getStatePetitionByVolunteer(request, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void getStatePetitionByVolunteerNegativeTest() throws OperationNotAllowedException {

		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getPetitionStateByVolunteer("oken", 1L)).thenReturn(attendance);

		ResponseEntity<?> res = activityController.getStatePetitionByVolunteer(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void getStatePetititionByOngTest() throws OperationNotAllowedException {

		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getPetitionStateByONG("oken", 1L, 1L)).thenReturn(attendance);

		ResponseEntity<?> res = activityController.getStatePetitionByONG(request, 1L, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void getStatePetititionByOngNegativeTest() throws OperationNotAllowedException {

		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");
		when(taskService.getPetitionStateByONG("oken", 1L, 1L)).thenReturn(attendance);

		ResponseEntity<?> res = activityController.getStatePetitionByONG(request, 1L, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
}
