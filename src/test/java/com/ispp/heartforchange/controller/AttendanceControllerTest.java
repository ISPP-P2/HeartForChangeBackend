package com.ispp.heartforchange.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.entity.AttendanceType;
import com.ispp.heartforchange.entity.Ong;
import com.ispp.heartforchange.entity.RolAccount;
import com.ispp.heartforchange.entity.Task;
import com.ispp.heartforchange.entity.TaskType;
import com.ispp.heartforchange.exceptions.OperationNotAllowedException;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.AttendanceServiceImpl;
import com.ispp.heartforchange.service.impl.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AttendanceControllerTest {

	@Mock
	private AttendanceServiceImpl attendanceService;

	@Mock
	private JwtUtils jwtUtils;
	
	@InjectMocks
	private AttendanceController attendanceontroller;
	
	
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
	
	@Test
	public void getByIdTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.getAttendanceById(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.getAttendanceById(request, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void getByIdNegativeTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.getAttendanceById(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.getAttendanceById(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void getAllAttendancesByTaskIdTest() throws OperationNotAllowedException {
				
		List<AttendanceDTO> attendance = new ArrayList<AttendanceDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.getAllAttendanceByIdTask(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.getAllAttendancesByTaskId(request, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void getAllAttendancesByTaskIdNetgativeTest() throws OperationNotAllowedException {
				
		List<AttendanceDTO> attendance = new ArrayList<AttendanceDTO>();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.getAllAttendanceByIdTask(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.getAllAttendancesByTaskId(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void createPetitionTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.createPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.createPetition(request, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void createPetitionNegativeTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.createPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.createPetition(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void deletePetitionTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.cancelPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.deletePetition(request, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void deletePetitionNegativeTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.cancelPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.deletePetition(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void acceptPetitionTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.acceptPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.acceptPetition(request, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void acceptPetitionNegativeTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.acceptPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.acceptPetition(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void denyPetitionTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.denyPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.denyPetition(request, 1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void denyPetitionNegativeTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.denyPetition(1L, "oken")).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.denyPetition(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void confirmAttendanceTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.confirmAttendance("oken", AttendanceType.PARCIAL, 1L)).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.confirmAttendance(request, 1L,1);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void confirmAttendanceNegativeTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.confirmAttendance("oken", AttendanceType.PARCIAL, 1L)).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.confirmAttendance(request, 1L,1);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void addBeneficiaryTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		when(attendanceService.addBeneficiary(1L,"oken", 1L)).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.addBeneficiary(request, 1L,1L);
		assertEquals(res, ResponseEntity.ok(attendance));
	}
	
	@Test
	public void addBeneficiaryNegativeTest() throws OperationNotAllowedException {
				
		AttendanceDTO attendance = new AttendanceDTO();
		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		when(attendanceService.addBeneficiary(1L,"oken", 1L)).thenReturn(attendance);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.addBeneficiary(request, 1L,1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteBeneficiaryTest() throws OperationNotAllowedException {
				

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.deleteBeneficiary(request, 1L, 1L);
		assertEquals(res, ResponseEntity.ok("Attendance Deleted"));
	}
	
	@Test
	public void deleteBeneficiaryNegativeTest() throws OperationNotAllowedException {
				

		
		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.deleteBeneficiary(request, 1L, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void deleteAttendanceByAttendanceTest() throws OperationNotAllowedException {
				

		when(jwtUtils.validateJwtToken("oken")).thenReturn(true);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.deleteAttendanceByAttendance(request, 1L);
		assertEquals(res, ResponseEntity.ok("Attendance Deleted"));
	}
	
	@Test
	public void deleteAttendanceByAttendanceNegativeTest() throws OperationNotAllowedException {
				

		when(jwtUtils.validateJwtToken("oken")).thenReturn(false);
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Authorization")).thenReturn("Bearertoken");

		ResponseEntity<?> res = attendanceontroller.deleteAttendanceByAttendance(request, 1L);
		assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
}
